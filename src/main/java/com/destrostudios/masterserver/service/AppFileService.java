package com.destrostudios.masterserver.service;

import com.destrostudios.masterserver.model.FileInfo;
import com.destrostudios.masterserver.database.schema.App;
import com.destrostudios.masterserver.database.schema.AppFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

@Service
public class AppFileService {

    public AppFileService(@Value("${destrostudios.apps-directory}") String appsDirectory) throws NoSuchAlgorithmException {
        this.appsDirectory = appsDirectory;
        messageDigestSha256 = MessageDigest.getInstance("SHA-256");
    }
    private String appsDirectory;
    private MessageDigest messageDigestSha256;

    public List<AppFile> generateAppFiles(App app, List<AppFile> existingAppFiles) throws IOException {
        List<AppFile> appFiles = new LinkedList<>();
        File appDirectory = new File(getAppDirectoryPath(app));
        addAppFiles(app, existingAppFiles, appFiles, appDirectory);
        return appFiles;
    }

    private void addAppFiles(App app, List<AppFile> existingAppFiles, List<AppFile> appFiles, File file) throws IOException {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                addAppFiles(app, existingAppFiles, appFiles, subFile);
            }
        } else {
            String path = file.getPath().substring(getAppDirectoryPath(app).length()).replace("\\", "/");
            AppFile appFile = existingAppFiles.stream()
                .filter(existingAppFile -> existingAppFile.getPath().equals(path))
                .findAny().orElseGet(() -> AppFile.builder()
                    .app(app)
                    .path(path)
                    .build());
            FileInfo fileInfo = getFileInfo(file);
            appFile.setSizeBytes(fileInfo.getSizeBytes());
            appFile.setChecksumSha256(fileInfo.getChecksumSha256());
            appFile.setDateTime(LocalDateTime.now());
            appFiles.add(appFile);
        }
    }

    private FileInfo getFileInfo(File file) throws IOException {
        byte[] buffer = new byte[8192];
        long sizeBytes = 0;
        int count;
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        while ((count = bufferedInputStream.read(buffer)) > 0) {
            sizeBytes += count;
            messageDigestSha256.update(buffer, 0, count);
        }
        bufferedInputStream.close();

        byte[] hash = messageDigestSha256.digest();
        String checksumSha256 = Base64.getEncoder().encodeToString(hash);

        return FileInfo.builder()
            .sizeBytes(sizeBytes)
            .checksumSha256(checksumSha256)
            .build();
    }

    public File getFile(AppFile appFile) {
        return new File(getAppDirectoryPath(appFile.getApp()) + appFile.getPath());
    }

    private String getAppDirectoryPath(App app) {
        return appsDirectory + app.getName() + "/";
    }
}
