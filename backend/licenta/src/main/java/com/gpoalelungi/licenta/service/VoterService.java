package com.gpoalelungi.licenta.service;

import com.gpoalelungi.licenta.exceptions.VoterNotFoundException;
import com.gpoalelungi.licenta.model.User;
import com.gpoalelungi.licenta.model.Voter;
import com.gpoalelungi.licenta.repository.VoterRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoterService {
  private final UserService userService;
  private final VoterRepository voterRepository;

  public Voter getVoterDetailsOfLoggedUser() {
    User user = userService.getLoggedUserDetails();
    return user.getVoter();
  }

  public List<Voter> getAllVoters() {
    log.info("Getting all voters");
    return voterRepository.findAll();
  }

  public Boolean markVoterAsRegistered(Long voterId) {
    log.info("Marking voter with id {} as registered", voterId);
    Voter voter = voterRepository.findById(voterId)
        .orElseThrow(() -> new VoterNotFoundException("Voter not found with id: " + voterId));

    voter.setIsRegistered(true);
    voterRepository.save(voter);
    return true;
  }
}
