package com.destrostudios.masterserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SetAppHighscoreDto {

    private String context;
    private AppHighscoreEvaluation evaluation;
    private long score;
    private String metadata;

}
