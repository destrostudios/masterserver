package com.destrostudios.masterserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Email {

    private String to;
    private String subject;
    private String text;

}
