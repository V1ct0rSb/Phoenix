package com.vb.phoenix.usuario.domain;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRabbit {

    private UUID userId;
    private String emailTo;
    private String subject;
    private String text;
}
