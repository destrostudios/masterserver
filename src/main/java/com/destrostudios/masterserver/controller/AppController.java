package com.destrostudios.masterserver.controller;

import com.destrostudios.masterserver.database.schema.App;
import com.destrostudios.masterserver.model.AppFilesDto;
import com.destrostudios.masterserver.service.AppService;
import com.destrostudios.masterserver.service.exceptions.AppAlreadyAddedException;
import com.destrostudios.masterserver.service.exceptions.AppFileNotFoundException;
import com.destrostudios.masterserver.service.exceptions.AppNotAddedException;
import com.destrostudios.masterserver.service.exceptions.AppNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/apps")
public class AppController {

    @Autowired
    private AppService appService;

    @GetMapping
    public List<App> getApps() {
        return appService.getApps();
    }

    @PostMapping("/{appId}/addToAccount")
    public void addToAccount(@RequestAttribute int userId, @PathVariable int appId) throws AppNotFoundException, AppAlreadyAddedException {
        appService.addToAccount(userId, appId);
    }

    @PostMapping("/{appId}/removeFromAccount")
    public void removeFromAccount(@RequestAttribute int userId, @PathVariable int appId) throws AppNotFoundException, AppNotAddedException {
        appService.removeFromAccount(userId, appId);
    }

    @PostMapping("/{appId}/updateFiles")
    public void updateFiles(@PathVariable int appId) throws AppNotFoundException, IOException {
        appService.updateFiles(appId);
    }

    @GetMapping("/{appId}/files")
    public AppFilesDto getFiles(@PathVariable int appId) throws AppNotFoundException {
        return appService.getFilesDto(appId);
    }

    @GetMapping("/file/{appFileId}")
    @ResponseBody
    public FileSystemResource getFile(@PathVariable int appFileId) throws AppFileNotFoundException {
        return appService.getFileSystemResource(appFileId);
    }
}
