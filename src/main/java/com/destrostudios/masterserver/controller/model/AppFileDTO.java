package com.destrostudios.masterserver.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AppFileDTO {

    private int id;
    private String path;
    private String checksumSha256;

}
