package com.destrostudios.masterserver.controller;

import com.destrostudios.masterserver.database.schema.App;
import com.destrostudios.masterserver.model.*;
import com.destrostudios.masterserver.service.AppService;
import com.destrostudios.masterserver.service.exceptions.*;
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
    @Autowired
    private AppHighscoreDtoMapper appHighscoreDtoMapper;

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

    @GetMapping("/{appId}/highscores")
    public List<AppHighscoreDto> getHighscores(@PathVariable int appId, @RequestParam AppHighscoreEvaluation evaluation, @RequestParam(required = false) Integer limitPerContext) throws BadRequestException, AppNotFoundException {
        return appHighscoreDtoMapper.map(appService.getHighscores(appId, evaluation, limitPerContext));
    }

    @GetMapping("/{appId}/highscores/{context}")
    public List<AppHighscoreDto> getHighscores(@PathVariable int appId, @PathVariable String context, @RequestParam AppHighscoreEvaluation evaluation, @RequestParam(required = false) Integer limit) throws BadRequestException, AppNotFoundException {
        return appHighscoreDtoMapper.map(appService.getHighscores(appId, context, evaluation, limit));
    }

    @PostMapping("/{appId}/setHighscore")
    public void setHighscore(@RequestAttribute int userId, @PathVariable int appId, @RequestBody SetAppHighscoreDto setAppHighscoreDto) throws BadRequestException, AppNotFoundException, NotAHighscoreException {
        appService.setHighscore(userId, appId, setAppHighscoreDto);
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
