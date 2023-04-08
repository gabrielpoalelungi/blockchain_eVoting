package com.gpoalelungi.licenta.auth;

import com.gpoalelungi.licenta.exceptions.InvalidIdentityCardException;
import com.gpoalelungi.licenta.exceptions.UserAlreadyExistsException;
import com.gpoalelungi.licenta.exceptions.UserNotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        try {
            return ResponseEntity.ok(authenticationService.register(request));
        } catch (UserAlreadyExistsException | InvalidIdentityCardException exc) {
            AuthenticationResponse response = new AuthenticationResponse(null, exc.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        try {
            return ResponseEntity.ok(authenticationService.login(request));
        } catch (UserNotFoundException e) {
            AuthenticationResponse response = new AuthenticationResponse(null, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
