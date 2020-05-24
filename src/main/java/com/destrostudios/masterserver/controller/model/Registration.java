package com.destrostudios.masterserver.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Registration {

    private String login;
    private String saltClient;
    private String hashedPassword;

}
