package com.gpoalelungi.licenta.service;

import com.gpoalelungi.licenta.exceptions.VoterNotFoundException;
import com.gpoalelungi.licenta.model.User;
import com.gpoalelungi.licenta.model.Voter;
import com.gpoalelungi.licenta.repository.VoterRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoterService {
  private final VoterRepository voterRepository;
  private final UserService userService;

  public Voter getVoterById(Long voterId) {
        return voterRepository.findById(voterId).orElseThrow(() -> new VoterNotFoundException("Voter not found with id: " + voterId));
  }

  public Voter getVoterDetailsOfLoggedUser() {
    User user = userService.getLoggedUserDetails();
    return user.getVoter();
  }
}
