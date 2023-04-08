package com.gpoalelungi.licenta.auth;

import com.gpoalelungi.licenta.model.IdentityCard;
import com.gpoalelungi.licenta.model.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String email;
    private String password;
    private String phoneNumber;
    private IdentityCard identityCard;
}
