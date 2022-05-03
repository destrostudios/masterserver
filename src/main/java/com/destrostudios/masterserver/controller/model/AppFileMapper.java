package com.destrostudios.masterserver.controller.model;

import com.destrostudios.masterserver.database.schema.AppFile;
import com.destrostudios.masterserver.database.schema.AppFileProtection;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppFileMapper {

    public AppFilesResponse mapResponse(List<AppFile> files, List<AppFileProtection> protections) {
        return AppFilesResponse.builder()
                .files(mapFiles(files))
                .protections(mapProtections(protections))
                .build();
    }

    private List<AppFileDTO> mapFiles(List<AppFile> appFiles) {
        return appFiles.stream()
                .map(this::mapFile)
                .collect(Collectors.toList());
    }

    private AppFileDTO mapFile(AppFile appFile) {
        return AppFileDTO.builder()
                .id(appFile.getId())
                .path(appFile.getPath())
                .sizeBytes(appFile.getSizeBytes())
                .checksumSha256(appFile.getChecksumSha256())
                .build();
    }

    private List<String> mapProtections(List<AppFileProtection> appFileProtections) {
        return appFileProtections.stream()
                .map(AppFileProtection::getPath)
                .collect(Collectors.toList());
    }
}
