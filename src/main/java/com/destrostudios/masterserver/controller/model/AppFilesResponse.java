package com.destrostudios.masterserver.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AppFilesResponse {

    private List<AppFileDTO> files;
    private List<String> protections;

}
