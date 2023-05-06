package com.gpoalelungi.licenta.service;

import com.gpoalelungi.licenta.exceptions.UserNotFoundException;
import com.gpoalelungi.licenta.model.IdentityCard;
import com.gpoalelungi.licenta.model.User;
import com.gpoalelungi.licenta.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final IdentityCardService identityCardService;

  public User getLoggedUserDetails() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = ((User) auth.getPrincipal()).getEmail();
    return userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new UserNotFoundException("User with this email was not found"));
  }

  public Boolean validateIdentityCard(IdentityCard identityCard) {
    return identityCardService.validateIdentityCard(identityCard);
  }
}
