package com.destrostudios.masterserver.controller;

import com.destrostudios.masterserver.controller.model.AppFileMapper;
import com.destrostudios.masterserver.controller.model.AppFilesResponse;
import com.destrostudios.masterserver.database.AppFileProtectionRepository;
import com.destrostudios.masterserver.database.AppFileRepository;
import com.destrostudios.masterserver.database.AppOwnershipRepository;
import com.destrostudios.masterserver.database.AppRepository;
import com.destrostudios.masterserver.database.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/apps")
public class AppController {

    @Autowired
    private AppRepository appRepository;
    @Autowired
    private AppOwnershipRepository appOwnershipRepository;
    @Autowired
    private AppFileRepository appFileRepository;
    @Autowired
    private AppFileProtectionRepository appFileProtectionRepository;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private AppFileService appFileService;
    @Autowired
    private AppFileMapper appFileMapper;

    @GetMapping
    public List<App> getApps() {
        return appRepository.findAll();
    }

    @GetMapping("/{appId}/addToAccount")
    public ResponseEntity<Void> addToAccount(@RequestHeader String sessionId, @PathVariable("appId") String appIdString) {
        int appId = Integer.parseInt(appIdString);
        User user = sessionService.getUser(sessionId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<App> appOptional = appRepository.findById(appId);
        if (appOptional.isPresent()) {
            App app = appOptional.get();
            if (user.getAppOwnerships().stream().noneMatch(ao -> ao.getApp().getId() == app.getId())) {
                AppOwnership appOwnership = AppOwnership.builder()
                        .user(user)
                        .app(app)
                        .dateTime(LocalDateTime.now())
                        .build();
                appOwnershipRepository.save(appOwnership);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/{appId}/removeFromAccount")
    public ResponseEntity<Void> removeFromAccount(@RequestHeader String sessionId, @PathVariable("appId") String appIdString) {
        int appId = Integer.parseInt(appIdString);
        User user = sessionService.getUser(sessionId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<App> appOptional = appRepository.findById(appId);
        if (appOptional.isPresent()) {
            App app = appOptional.get();
            AppOwnership appOwnership = user.getAppOwnerships().stream().filter(ao -> ao.getApp().getId() == app.getId()).findAny().orElse(null);
            if (appOwnership != null) {
                appOwnershipRepository.delete(appOwnership);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/{appId}/updateFiles")
    public void updateFiles(@PathVariable("appId") String appIdString) throws IOException {
        int appId = Integer.parseInt(appIdString);
        App app = appRepository.findById(appId).get();
        List<AppFile> oldAppFiles = appFileRepository.findByApp(app);
        List<AppFile> newAppFiles = appFileService.generateAppFiles(app, oldAppFiles);
        List<AppFile> removedAppFiles = new LinkedList<>(oldAppFiles);
        removedAppFiles.removeAll(newAppFiles);
        appFileRepository.deleteAll(removedAppFiles);
        appFileRepository.saveAll(newAppFiles);
    }

    @GetMapping("/{appId}/files")
    public ResponseEntity<AppFilesResponse> getFiles(@PathVariable("appId") String appIdString) {
        int appId = Integer.parseInt(appIdString);
        Optional<App> appOptional = appRepository.findById(appId);
        if (appOptional.isPresent()) {
            App app = appOptional.get();
            List<AppFile> appFiles = appFileRepository.findByApp(app);
            List<AppFileProtection> appFileProtections = appFileProtectionRepository.findByApp(app);
            AppFilesResponse response = appFileMapper.mapResponse(appFiles, appFileProtections);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/file/{appFileId}")
    @ResponseBody
    public FileSystemResource getFile(@PathVariable("appFileId") String appFileIdString) {
        int appFileId = Integer.parseInt(appFileIdString);
        Optional<AppFile> appFileOptional = appFileRepository.findById(appFileId);
        if (appFileOptional.isPresent()) {
            AppFile appFile = appFileOptional.get();
            return new FileSystemResource(appFileService.getFile(appFile));
        } else {
            return null;
        }
    }
}
