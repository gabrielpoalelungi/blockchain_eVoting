package com.gpoalelungi.licenta.auth;

import com.gpoalelungi.licenta.model.IdentityCard;
import com.gpoalelungi.licenta.model.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String email;
    private String password;
    private String phoneNumber;
    private String CNP;
    private String idCardNumber;
    private Date expirationDate;

}
