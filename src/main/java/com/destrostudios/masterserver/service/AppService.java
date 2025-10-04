package com.destrostudios.masterserver.service;

import com.destrostudios.masterserver.database.*;
import com.destrostudios.masterserver.database.schema.*;
import com.destrostudios.masterserver.model.AppFilesDto;
import com.destrostudios.masterserver.model.AppFilesDtoMapper;
import com.destrostudios.masterserver.model.SetAppHighscoreDto;
import com.destrostudios.masterserver.model.AppHighscoreEvaluation;
import com.destrostudios.masterserver.service.annotations.BaseTransactional;
import com.destrostudios.masterserver.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class AppService {

    @Autowired
    private AppRepository appRepository;
    @Autowired
    private AppOwnershipRepository appOwnershipRepository;
    @Autowired
    private AppHighscoreRepository appHighscoreRepository;
    @Autowired
    private AppFileRepository appFileRepository;
    @Autowired
    private AppFileProtectionRepository appFileProtectionRepository;
    @Autowired
    private AppFileService appFileService;
    @Autowired
    private AppFilesDtoMapper appFilesDtoMapper;

    public List<App> getApps() {
        return appRepository.findAll();
    }

    @BaseTransactional
    public void addToAccount(int userId, int appId) throws AppNotFoundException, AppAlreadyAddedException {
        if (!appRepository.existsById(appId)) {
            throw new AppNotFoundException();
        }
        List<AppOwnership> appOwnerships = appOwnershipRepository.findByUserId(userId);
        if (appOwnerships.stream().anyMatch(ao -> ao.getApp().getId() == appId)) {
            throw new AppAlreadyAddedException();
        }
        AppOwnership appOwnership = AppOwnership.builder()
            .user(User.builder()
                .id(userId)
                .build())
            .app(App.builder()
                .id(appId)
                .build())
            .dateTime(LocalDateTime.now())
            .build();
        appOwnershipRepository.save(appOwnership);
    }

    @BaseTransactional
    public void removeFromAccount(int userId, int appId) throws AppNotFoundException, AppNotAddedException {
        if (!appRepository.existsById(appId)) {
            throw new AppNotFoundException();
        }
        List<AppOwnership> appOwnerships = appOwnershipRepository.findByUserId(userId);
        AppOwnership appOwnership = appOwnerships.stream().filter(ao -> ao.getApp().getId() == appId).findAny().orElse(null);
        if (appOwnership == null) {
            throw new AppNotAddedException();
        }
        appOwnershipRepository.delete(appOwnership);
    }

    public List<AppHighscore> getHighscores(int appId) throws AppNotFoundException {
        if (!appRepository.existsById(appId)) {
            throw new AppNotFoundException();
        }
        return appHighscoreRepository.findByAppId(appId);
    }

    public List<AppHighscore> getHighscores(int appId, String context) throws AppNotFoundException {
        if (!appRepository.existsById(appId)) {
            throw new AppNotFoundException();
        }
        return appHighscoreRepository.findByAppIdAndContext(appId, context);
    }

    @BaseTransactional
    public void setHighscore(int userId, int appId, SetAppHighscoreDto setAppHighscoreDto) throws BadRequestException, AppNotFoundException, NotAHighscoreException {
        if (isEmpty(setAppHighscoreDto.getContext()) || (setAppHighscoreDto.getEvaluation() == null)) {
            throw new BadRequestException();
        }
        if (!appRepository.existsById(appId)) {
            throw new AppNotFoundException();
        }
        AppHighscore appHighscore = appHighscoreRepository.findByAppIdAndContextAndUserId(appId, setAppHighscoreDto.getContext(), userId);
        if (appHighscore == null) {
            appHighscoreRepository.save(AppHighscore.builder()
                .user(User.builder()
                    .id(userId)
                    .build())
                .app(App.builder()
                    .id(appId)
                    .build())
                .context(setAppHighscoreDto.getContext())
                .score(setAppHighscoreDto.getScore())
                .metadata(setAppHighscoreDto.getMetadata())
                .dateTime(LocalDateTime.now())
                .build());
        } else {
            if (((setAppHighscoreDto.getEvaluation() == AppHighscoreEvaluation.HIGHER) && (setAppHighscoreDto.getScore() <= appHighscore.getScore()))
             || ((setAppHighscoreDto.getEvaluation() == AppHighscoreEvaluation.LOWER) && (setAppHighscoreDto.getScore() >= appHighscore.getScore()))) {
                throw new NotAHighscoreException();
            }
            appHighscore.setScore(setAppHighscoreDto.getScore());
            appHighscore.setMetadata(appHighscore.getMetadata());
            appHighscore.setDateTime(LocalDateTime.now());
            appHighscoreRepository.save(appHighscore);
        }
    }

    @BaseTransactional
    public void updateFiles(int appId) throws AppNotFoundException, IOException {
        App app = getApp(appId);
        List<AppFile> oldAppFiles = appFileRepository.findByApp(app);
        List<AppFile> newAppFiles = appFileService.generateAppFiles(app, oldAppFiles);
        List<AppFile> removedAppFiles = new LinkedList<>(oldAppFiles);
        removedAppFiles.removeAll(newAppFiles);
        appFileRepository.deleteAll(removedAppFiles);
        appFileRepository.saveAll(newAppFiles);
    }

    public AppFilesDto getFilesDto(int appId) throws AppNotFoundException {
        App app = getApp(appId);
        List<AppFile> appFiles = appFileRepository.findByApp(app);
        List<AppFileProtection> appFileProtections = appFileProtectionRepository.findByApp(app);
        return appFilesDtoMapper.mapFiles(appFiles, appFileProtections);
    }

    public FileSystemResource getFileSystemResource(int appFileId) throws AppFileNotFoundException {
        AppFile appFile = appFileRepository.findById(appFileId).orElseThrow(AppFileNotFoundException::new);
        return new FileSystemResource(appFileService.getFile(appFile));
    }

    public App getApp(int appId) throws AppNotFoundException {
        return appRepository.findById(appId).orElseThrow(AppNotFoundException::new);
    }
}
