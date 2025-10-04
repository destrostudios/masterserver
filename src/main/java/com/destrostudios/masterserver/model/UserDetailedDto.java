package com.destrostudios.masterserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserDetailedDto {

    private int id;
    private String login;
    private List<Integer> ownedAppIds;

}
