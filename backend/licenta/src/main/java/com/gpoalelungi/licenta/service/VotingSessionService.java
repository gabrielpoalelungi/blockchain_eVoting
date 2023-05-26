package com.gpoalelungi.licenta.service;

import com.gpoalelungi.licenta.contract.Election;
import com.gpoalelungi.licenta.contract.ElectionContractService;
import com.gpoalelungi.licenta.exceptions.InvalidVoteException;
import com.gpoalelungi.licenta.exceptions.VotingSessionNotFinishedException;
import com.gpoalelungi.licenta.exceptions.VotingSessionNotFoundException;
import com.gpoalelungi.licenta.exceptions.VotingSessionNotStartedException;
import com.gpoalelungi.licenta.exceptions.VotingSessionPrivateKeyNotReleasedException;
import com.gpoalelungi.licenta.model.VotingSession;
import com.gpoalelungi.licenta.model.VotingSessionStatus;
import com.gpoalelungi.licenta.repository.VotingSessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import java.security.MessageDigest;

@Service
@Slf4j
@RequiredArgsConstructor
public class VotingSessionService {
  private final VotingSessionRepository votingSessionRepository;
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

  public void updateVotingSessionStatus(VotingSessionStatus votingSessionStatus) {
    VotingSession votingSession = getVotingSession();
    votingSession.setVotingSessionStatus(votingSessionStatus);
    votingSessionRepository.save(votingSession);
  }

  public VotingSession editVotingSessionStartingEndingAt(Date newStartingAt, Date newEndingAt) {
    VotingSession votingSession = getVotingSession();
    votingSession.setStartingAt(newStartingAt);
    votingSession.setEndingAt(newEndingAt);
    return votingSessionRepository.save(votingSession);
  }

  public VotingSession getVotingSession() {
    return votingSessionRepository.findFirstByVotingSessionPublicKeyNotNull()
            .orElseThrow(() -> new VotingSessionNotFoundException("No voting session found"));
  }

  public List<Election.Vote> getAllVotes() throws Exception {
    VotingSession votingSession = getVotingSession();

    if (votingSession.getVotingSessionStatus() != VotingSessionStatus.FINISHED) {
      throw new VotingSessionNotStartedException("Cannot get votes. Voting session NOT FINISHED yet.");
    }

    try {
      return electionContractService.getAllVotes();
    } catch (Exception e) {
      log.error("Error while getting votes from smart contract: {}", e.getMessage());
      throw e;
    }
  }

  public String startVote() throws Exception {
    try {
      String message = electionContractService.startVote();
      updateVotingSessionStatus(VotingSessionStatus.IN_PROGRESS);
      return message;
    } catch (Exception e) {
      log.error("Error while starting vote: {}", e.getMessage());
      throw e;
    }
  }

  public String endVote() throws Exception {
    VotingSession votingSession = getVotingSession();

    if (votingSession.getVotingSessionStatus() == VotingSessionStatus.NOT_STARTED) {
        throw new VotingSessionNotStartedException("Cannot end vote. Voting session NOT STARTED yet.");
    }

    try {
      String message = electionContractService.endVote();
      releaseVotingSessionPrivateKey();
      updateVotingSessionStatus(VotingSessionStatus.FINISHED);
      return message;
    } catch (Exception e) {
      log.error("Error while finishing vote: {}", e.getMessage());
      throw e;
    }
  }

  public String decryptVote(String publicKey, String voteToDecrypt, String signature) throws Exception {
    VotingSession votingSession = getVotingSession();

    if (votingSession.getReleasePrivateKey() == null) {
        throw new VotingSessionPrivateKeyNotReleasedException("Private key not released yet");
    }

    if(!isVoteValid(signature, publicKey, voteToDecrypt)) {
      throw new InvalidVoteException("Invalid vote found");
    }

    byte[] privateKeyBytes = Base64.getDecoder().decode(votingSession.getReleasePrivateKey());

    KeyFactory privateKeyFactory = KeyFactory.getInstance("RSA");
    EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
    PrivateKey privateKey = privateKeyFactory.generatePrivate(privateKeySpec);

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);

    return new String(cipher.doFinal(Base64.getDecoder().decode(voteToDecrypt)));
  }

  // TODO: check in DB if the public Key exists
  private Boolean isVoteValid(String voteSignature, String voterPublicKey, String encryptedVote) throws Exception{
    byte[] voterPublicKeyBytes = Base64.getDecoder().decode(voterPublicKey);
    KeyFactory publicKeyFactory = KeyFactory.getInstance("RSA");
    EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(voterPublicKeyBytes);
    PublicKey publicKey = publicKeyFactory.generatePublic(publicKeySpec);

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, publicKey);

    byte[] decryptedSignature = cipher.doFinal(Base64.getDecoder().decode(voteSignature));

    String decryptedSignatureToString = bytesToHex(decryptedSignature);
    String encryptedVoteHash = getSha256Hash(voterPublicKey + encryptedVote);

    return encryptedVoteHash.equals(decryptedSignatureToString);
  }

  private String getSha256Hash(String plainText) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] encodedHash = digest.digest(plainText.getBytes(StandardCharsets.UTF_8));

    return bytesToHex(encodedHash);
  }

  private String bytesToHex(byte[] input) {
    // Convert the byte array to a hexadecimal string
    StringBuilder hexString = new StringBuilder();
    for (byte b : input) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }

  private byte[] readPrivateKey(Long votingSessionId) throws IOException {
    FileInputStream fis = new FileInputStream(String.format("privateKey-session%d.txt", votingSessionId));
    return fis.readAllBytes();
  }
}
