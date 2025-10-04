package com.destrostudios.masterserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AppHighscoreDto {

    private int id;
    private AppBasicDto app;
    private String context;
    private UserBasicDto user;
    private int score;
    private String metadata;
    private LocalDateTime dateTime;

}
