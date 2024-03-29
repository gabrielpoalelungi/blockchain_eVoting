package com.gpoalelungi.licenta.controller;

import com.gpoalelungi.licenta.exceptions.UserNotFoundException;
import com.gpoalelungi.licenta.model.User;
import com.gpoalelungi.licenta.dto.UserResponse;
import com.gpoalelungi.licenta.service.UserService;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/logged-user")
  public ResponseEntity<UserResponse> getLoggedUserDetails() {
    try {
      User loggedUser = userService.getLoggedUserDetails();
      UserResponse userResponse = UserResponse.builder()
          .email(loggedUser.getEmail())
          .phoneNumber(loggedUser.getPhoneNumber())
          .build();

      return ResponseEntity.ok(userResponse);
    } catch (UserNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
