package com.gpoalelungi.licenta.service;

import com.gpoalelungi.licenta.model.User;
import com.gpoalelungi.licenta.model.Voter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoterService {
  private final UserService userService;

  public Voter getVoterDetailsOfLoggedUser() {
    User user = userService.getLoggedUserDetails();
    return user.getVoter();
  }
}
