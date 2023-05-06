package com.gpoalelungi.licenta.service;

import com.gpoalelungi.licenta.contract.ElectionContractService;
import com.gpoalelungi.licenta.exceptions.VoterNotFoundException;
import com.gpoalelungi.licenta.exceptions.VotingSessionNotStartedException;
import com.gpoalelungi.licenta.model.User;
import com.gpoalelungi.licenta.model.Voter;
import com.gpoalelungi.licenta.model.VotingSession;
import com.gpoalelungi.licenta.model.VotingSessionStatus;
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
  private final VotingSessionService votingSessionService;
  private final ElectionContractService electionContractService;

  public Voter getVoterDetailsOfLoggedUser() {
    User user = userService.getLoggedUserDetails();
    return user.getVoter();
  }

  public void addAllVotersToContract() throws Exception {
    VotingSession votingSession = votingSessionService.getVotingSession();

    if (votingSession.getVotingSessionStatus() != VotingSessionStatus.NOT_STARTED) {
      throw new VotingSessionNotStartedException("Cannot add voters to smart contract. Voting session NOT STARTED yet.");
    }

    try {
      List<Voter> voters = getAllVoters();
      for (Voter voter: voters) {
        Boolean isRegistered = electionContractService.addVoter(voter.getPublicKey());
        if (!isRegistered) {
          log.error("Voter with id={} was not registered", voter.getId());
        } else {
          markVoterAsRegistered(voter.getId());
        }
      }
    } catch (Exception e) {
      log.error("Error while adding voters to smart contract: {}", e.getMessage());
      throw e;
    }
  }

  private List<Voter> getAllVoters() {
    log.info("Getting all voters");
    return voterRepository.findAll();
  }

  private void markVoterAsRegistered(Long voterId) {
    log.info("Marking voter with id {} as registered", voterId);
    Voter voter = voterRepository.findById(voterId)
        .orElseThrow(() -> new VoterNotFoundException("Voter not found with id: " + voterId));

    voter.setIsRegistered(true);
    voterRepository.save(voter);
  }
}
