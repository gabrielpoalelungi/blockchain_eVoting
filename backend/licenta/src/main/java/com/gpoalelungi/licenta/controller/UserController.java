package com.gpoalelungi.licenta.controller;

import com.gpoalelungi.licenta.auth.AuthenticationResponse;
import com.gpoalelungi.licenta.auth.RegisterRequest;
import com.gpoalelungi.licenta.model.User;
import com.gpoalelungi.licenta.model.UserDetailsResponse;
import com.gpoalelungi.licenta.service.UserService;

import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/vote")
  public ResponseEntity<Boolean> registerUserHasVoted() {
    return ResponseEntity.ok(userService.registerUserHasVoted());
  }

  @GetMapping("/get-user-details")
  public ResponseEntity<UserDetailsResponse> getUserDetails() {
    return ResponseEntity.ok(userService.getUserDetails());
  }
}
