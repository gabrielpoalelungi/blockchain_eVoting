package com.gpoalelungi.licenta.service;

import com.gpoalelungi.licenta.contract.ElectionContractService;
import com.gpoalelungi.licenta.exceptions.VotingSessionNotFinishedException;
import com.gpoalelungi.licenta.exceptions.VotingSessionNotFoundException;
import com.gpoalelungi.licenta.model.Voter;
import com.gpoalelungi.licenta.model.VotingSession;
import com.gpoalelungi.licenta.model.VotingSessionStatus;
import com.gpoalelungi.licenta.repository.VotingSessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VotingSessionService {
  private final VotingSessionRepository votingSessionRepository;
  private final VoterService voterService;
  private final ElectionContractService electionContractService;

  public VotingSession createVotingSession(Date startingAt, Date endingAt) throws NoSuchAlgorithmException, IOException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();

    PrivateKey privateKey = keyPair.getPrivate();
    byte[] privateKeyBytes = privateKey.getEncoded();
    FileOutputStream fos = new FileOutputStream(String.format("privateKey-session%d.txt", votingSessionRepository.getCurrentVal() + 1));
    fos.write(privateKeyBytes);
    fos.close();

    VotingSession votingSessionToBeSaved = VotingSession.builder()
        .votingSessionPublicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()))
        .votingSessionStatus(VotingSessionStatus.NOT_STARTED)
        .startingAt(startingAt)
        .endingAt(endingAt)
        .build();

    return votingSessionRepository.save(votingSessionToBeSaved);
  }

  public void releaseVotingSessionPrivateKey(Long votingSessionId) throws IOException {
    VotingSession votingSession = votingSessionRepository.findById(votingSessionId)
        .orElseThrow(() -> new VotingSessionNotFoundException("Voting session not found with id: " + votingSessionId));
    if (votingSession.getVotingSessionStatus() != VotingSessionStatus.FINISHED) {
      throw new VotingSessionNotFinishedException("Voting session with id=" + votingSessionId + " NOT FINISHED YET");
    }

    byte[] privateKeyBytes = readPrivateKey(votingSessionId);

    votingSession.setReleasePrivateKey(Base64.getEncoder().encodeToString(privateKeyBytes));
    votingSessionRepository.save(votingSession);
  }

  public void updateVotingSessionStatus(Long votingSessionId, VotingSessionStatus votingSessionStatus) {
    VotingSession votingSession = votingSessionRepository.findById(votingSessionId)
        .orElseThrow(() -> new VotingSessionNotFoundException("Voting session not found with id=" + votingSessionId));
    votingSession.setVotingSessionStatus(votingSessionStatus);
    votingSessionRepository.save(votingSession);
  }

  public VotingSession editVotingSessionStartingEndingAt(Long votingSessionId, Date newStartingAt, Date newEndingAt) {
    VotingSession votingSession = votingSessionRepository.findById(votingSessionId)
        .orElseThrow(() -> new VotingSessionNotFoundException("Voting session not found with id=" + votingSessionId));
    votingSession.setStartingAt(newStartingAt);
    votingSession.setEndingAt(newEndingAt);
    return votingSessionRepository.save(votingSession);
  }

  public VotingSession getVotingSession() {
    return votingSessionRepository.findFirstByVotingSessionPublicKeyNotNull()
            .orElseThrow(() -> new VotingSessionNotFoundException("No voting session found"));
  }

  public List<Voter> addAllVotersToContract() throws Exception {
    List<Voter> voters = voterService.getAllVoters();
    List<Voter> unregisteredVoters = new ArrayList<>();
    for (Voter voter: voters) {
      Boolean isRegistered = electionContractService.addVoter(voter.getPublicKey());
      if (!isRegistered) {
        log.error("Voter with id={} was not registered", voter.getId());
        unregisteredVoters.add(voter);
      }
    }
    return unregisteredVoters;
  }

  private byte[] readPrivateKey(Long votingSessionId) throws IOException {
    FileInputStream fis = new FileInputStream(String.format("privateKey-session%d.txt", votingSessionId));
    return fis.readAllBytes();
  }

}
