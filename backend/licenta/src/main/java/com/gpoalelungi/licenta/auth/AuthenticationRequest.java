package com.gpoalelungi.licenta.auth;

import com.gpoalelungi.licenta.model.IdentityCard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {

    private String email;
    private String password;
}
