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

  public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
  }

  public User getLoggedUserDetails() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = ((User) auth.getPrincipal()).getEmail();
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new UserNotFoundException("User with this email was not found"));

    return user;
  }



  //TODO: implement this method
  public boolean validateIdentityCard(IdentityCard identityCard) {
    return true;
  }
}
