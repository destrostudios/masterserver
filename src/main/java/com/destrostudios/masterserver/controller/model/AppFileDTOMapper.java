package com.destrostudios.masterserver.controller.model;

import com.destrostudios.masterserver.database.schema.AppFile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppFileDTOMapper {

    public List<AppFileDTO> map(List<AppFile> appFiles) {
        return appFiles.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public AppFileDTO map(AppFile appFile) {
        return AppFileDTO.builder()
                .id(appFile.getId())
                .path(appFile.getPath())
                .checksumSha256(appFile.getChecksumSha256())
                .build();
    }
}
