package com.gpoalelungi.licenta.service;

import com.gpoalelungi.licenta.contract.ElectionContractService;
import com.gpoalelungi.licenta.exceptions.InvalidVoteException;
import com.gpoalelungi.licenta.exceptions.VotingSessionNotFinishedException;
import com.gpoalelungi.licenta.exceptions.VotingSessionNotFoundException;
import com.gpoalelungi.licenta.exceptions.VotingSessionNotStartedException;
import com.gpoalelungi.licenta.exceptions.VotingSessionPrivateKeyNotReleasedException;
import com.gpoalelungi.licenta.model.Voter;
import com.gpoalelungi.licenta.model.VotingSession;
import com.gpoalelungi.licenta.model.VotingSessionStatus;
import com.gpoalelungi.licenta.repository.VotingSessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

@Service
@Slf4j
@RequiredArgsConstructor
public class VotingSessionService {
  private final VotingSessionRepository votingSessionRepository;
  private final VoterService voterService;
  private final ElectionContractService electionContractService;

  private final PasswordEncoder passwordEncoder;

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

  public void releaseVotingSessionPrivateKey() throws IOException {
    VotingSession votingSession = votingSessionRepository.findFirstByVotingSessionPublicKeyNotNull()
        .orElseThrow(() -> new VotingSessionNotFoundException("Voting session not found"));
    if (votingSession.getVotingSessionStatus() != VotingSessionStatus.FINISHED) {
      throw new VotingSessionNotFinishedException("Voting session NOT FINISHED YET");
    }

    byte[] privateKeyBytes = readPrivateKey(votingSession.getId());

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


  public void addAllVotersToContract() throws Exception {
    VotingSession votingSession = votingSessionRepository.findFirstByVotingSessionPublicKeyNotNull()
        .orElseThrow(() -> new VotingSessionNotFoundException("Voting session not found"));

    if (votingSession.getVotingSessionStatus() != VotingSessionStatus.NOT_STARTED) {
      throw new VotingSessionNotStartedException("Cannot add voters to smart contract. Voting session NOT STARTED yet.");
    }

    try {
      List<Voter> voters = voterService.getAllVoters();
      for (Voter voter: voters) {
        Boolean isRegistered = electionContractService.addVoter(voter.getPublicKey());
        if (!isRegistered) {
          log.error("Voter with id={} was not registered", voter.getId());
        } else {
          voterService.markVoterAsRegistered(voter.getId());
        }
      }
    } catch (Exception e) {
      log.error("Error while adding voters to smart contract: {}", e.getMessage());
      throw e;
    }
  }

  public List getAllVotes() throws Exception {
    VotingSession votingSession = votingSessionRepository.findFirstByVotingSessionPublicKeyNotNull()
        .orElseThrow(() -> new VotingSessionNotFoundException("Voting session not found"));

    if (votingSession.getVotingSessionStatus() != VotingSessionStatus.FINISHED) {
      throw new VotingSessionNotStartedException("Cannot get votes. Voting session NOT FINISHED yet.");
    }

    try {
      return electionContractService.getAllVotes();
    } catch (Exception e) {
      log.error("Error while adding voters to smart contract: {}", e.getMessage());
      throw e;
    }
  }

  public String startVote() throws Exception {
    VotingSession votingSession = votingSessionRepository.findFirstByVotingSessionPublicKeyNotNull()
            .orElseThrow(() -> new VotingSessionNotFoundException("Voting session not found"));

    try {
      electionContractService.startVote();
      votingSession.setVotingSessionStatus(VotingSessionStatus.IN_PROGRESS);
      votingSessionRepository.save(votingSession);
      return "Vote started successfully";
    } catch (Exception e) {
      log.error("Error while starting vote: {}", e.getMessage());
      throw e;
    }
  }

  public String endVote() throws Exception {
    VotingSession votingSession = votingSessionRepository.findFirstByVotingSessionPublicKeyNotNull()
        .orElseThrow(() -> new VotingSessionNotFoundException("Voting session not found"));

    try {
      electionContractService.endVote();
      votingSession.setVotingSessionStatus(VotingSessionStatus.FINISHED);
      votingSessionRepository.save(votingSession);
      return "Vote finished successfully";
    } catch (Exception e) {
      log.error("Error while finishing vote: {}", e.getMessage());
      throw e;
    }
  }

  private byte[] readPrivateKey(Long votingSessionId) throws IOException {
    FileInputStream fis = new FileInputStream(String.format("privateKey-session%d.txt", votingSessionId));
    return fis.readAllBytes();
  }

  public String decryptVote(String publicKey, String voteToDecrypt, String signature) throws Exception {
    VotingSession votingSession = votingSessionRepository.findFirstByVotingSessionPublicKeyNotNull()
        .orElseThrow(() -> new VotingSessionNotFoundException("Voting session not found"));

    if (votingSession.getReleasePrivateKey() == null) {
        throw new VotingSessionPrivateKeyNotReleasedException("Private key not released yet");
    }

    if(!isVoteValid(signature, publicKey, voteToDecrypt)) {
      throw new InvalidVoteException("Invalid vote found");
    }

    byte[] privateKeyBytes = Base64.getDecoder().decode(votingSession.getReleasePrivateKey());

    KeyFactory privateKeyFactory = KeyFactory.getInstance("RSA");
    EncodedKeySpec privateKeySpec = new X509EncodedKeySpec(privateKeyBytes);
    PrivateKey privateKey = privateKeyFactory.generatePrivate(privateKeySpec);

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);

    return new String(cipher.doFinal(Base64.getDecoder().decode(voteToDecrypt)));
  }

  public Boolean isVoteValid(String voteSignature, String voterPublicKey, String encryptedVote) throws Exception{
    byte[] voterPublicKeyBytes = Base64.getDecoder().decode(voterPublicKey);
    KeyFactory publicKeyFactory = KeyFactory.getInstance("RSA");
    EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(voterPublicKeyBytes);
    PublicKey publicKey = publicKeyFactory.generatePublic(publicKeySpec);

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, publicKey);

    String decryptedSignature = new String(cipher.doFinal(Base64.getDecoder().decode(voteSignature)));

    return BCrypt.checkpw(voterPublicKey + encryptedVote, decryptedSignature);
  }

}
