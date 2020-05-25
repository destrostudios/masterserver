package com.destrostudios.masterserver.controller;

import com.destrostudios.masterserver.database.schema.App;
import com.destrostudios.masterserver.database.schema.AppFile;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

@Service
public class AppFileService {

    AppFileService() throws NoSuchAlgorithmException {
        messageDigestSha256 = MessageDigest.getInstance("SHA-256");
    }
    private MessageDigest messageDigestSha256;
    public static final String APPS_DIRECTORY = "./apps/";

    List<AppFile> generateAppFiles(App app) throws IOException {
        List<AppFile> appFiles = new LinkedList<>();
        File appDirectory = new File(getAppDirectoryPath(app));
        addAppFiles(app, appFiles, appDirectory);
        return appFiles;
    }

    private void addAppFiles(App app, List<AppFile> appFiles, File file) throws IOException {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                addAppFiles(app, appFiles, subFile);
            }
        } else {
            String path = file.getPath().substring(getAppDirectoryPath(app).length());
            String checksumSha256 = getChecksum(file, messageDigestSha256);
            AppFile appFile = AppFile.builder()
                    .app(app)
                    .path(path)
                    .checksumSha256(checksumSha256)
                    .build();
            appFiles.add(appFile);
        }
    }

    private String getAppDirectoryPath(App app) {
        return APPS_DIRECTORY + app.getName() + "/";
    }

    private static String getChecksum(File file, MessageDigest messageDigest) throws IOException {
        byte[] buffer = new byte[8192];
        int count;
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        while ((count = bufferedInputStream.read(buffer)) > 0) {
            messageDigest.update(buffer, 0, count);
        }
        bufferedInputStream.close();

        byte[] hash = messageDigest.digest();
        return Base64.getEncoder().encodeToString(hash);
    }

    public File getFile(AppFile appFile) {
        return new File(getAppDirectoryPath(appFile.getApp()) + appFile.getPath());
    }
}
