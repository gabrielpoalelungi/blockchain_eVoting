package com.gpoalelungi.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IdentityCard {

    Citizenship citizenship;
    String CNP;
    String idCardNumber;
    LocalDate idCardExpirationDate;

    enum Citizenship {
        ROU,
        ANY
    }
}


