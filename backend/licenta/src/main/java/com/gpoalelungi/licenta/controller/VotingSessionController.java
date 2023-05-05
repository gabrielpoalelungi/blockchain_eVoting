package com.gpoalelungi.licenta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpoalelungi.licenta.contract.ElectionContractService;
import com.gpoalelungi.licenta.dto.VoteDto;
import com.gpoalelungi.licenta.dto.VotingSessionRequest;
import com.gpoalelungi.licenta.dto.VotingSessionResponse;
import com.gpoalelungi.licenta.exceptions.VotingSessionNotFoundException;
import com.gpoalelungi.licenta.model.VotingSession;
import com.gpoalelungi.licenta.model.VotingSessionStatus;
import com.gpoalelungi.licenta.service.VotingSessionService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.ws.rs.QueryParam;

import java.util.List;

@RestController
@RequestMapping("/voting-session")
@RequiredArgsConstructor
public class VotingSessionController {
  private final VotingSessionService votingSessionService;
  private final ElectionContractService electionContractService;
  private final ObjectMapper objectMapper;

  @PostMapping("create")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> createVotingSession(@RequestBody VotingSessionRequest votingSessionRequest) {
    try {
      VotingSession votingSession = votingSessionService.createVotingSession(votingSessionRequest.startingAt, votingSessionRequest.endingAt);
      VotingSessionResponse votingSessionResponse = objectMapper.convertValue(votingSession, VotingSessionResponse.class);

      return ResponseEntity.ok(votingSessionResponse);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("release-private-key")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> releaseVotingSessionPrivateKey() {
    try {
      votingSessionService.releaseVotingSessionPrivateKey();
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("{id}/update-status")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> updateStatusVotingSession(@PathVariable("id") Long votingSessionId, @QueryParam("status") VotingSessionStatus newStatus) {
    try {
      votingSessionService.updateVotingSessionStatus(votingSessionId, newStatus);
      return ResponseEntity.ok().build();
    } catch (VotingSessionNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("{id}/update-dates")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> updateVotingSessionDates(@PathVariable("id") Long votingSessionId, @RequestBody VotingSessionRequest newVotingSessionDates) {
    try {
      votingSessionService.editVotingSessionStartingEndingAt(votingSessionId, newVotingSessionDates.startingAt, newVotingSessionDates.endingAt);
      return ResponseEntity.ok().build();
    } catch (VotingSessionNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("")
  public ResponseEntity<?> getVotingSession() {
    try {
      VotingSession votingSession = votingSessionService.getVotingSession();
      return ResponseEntity.ok(votingSession);
    } catch (VotingSessionNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("add-all-voters")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> addAllVoters() {
    try {
      votingSessionService.addAllVotersToContract();
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("get-votes")
  public ResponseEntity<?> getAllVotes() {
    try {
      List votes = votingSessionService.getAllVotes();
      return ResponseEntity.ok(votes);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/is-vote-started")
  public ResponseEntity<Boolean> isVoteStarted() throws Exception {
    return  ResponseEntity.ok(electionContractService.isVoteStarted());
  }

  @GetMapping("/is-vote-finished")
  public ResponseEntity<Boolean> isVoteFinished() throws Exception {
    return ResponseEntity.ok(electionContractService.isVoteFinished());
  }

  @PostMapping("/start-vote")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<String> doStartVote() {
    try {
      String message = votingSessionService.startVote();
      return ResponseEntity.ok(message);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/end-vote")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<String> doEndVote() {
    try {
      String message = votingSessionService.endVote();
      return ResponseEntity.ok(message);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/test-decrypt-vote")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<String> decryptVote(@RequestBody VoteDto voteRequest) {
    try {
      String message = votingSessionService.decryptVote(voteRequest.getPublicKey(), voteRequest.getEncryptedVote(), voteRequest.getSignature());
      return ResponseEntity.ok(message);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
