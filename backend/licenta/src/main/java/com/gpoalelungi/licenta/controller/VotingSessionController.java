package com.gpoalelungi.licenta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@RestController
@RequestMapping("/voting-session")
@RequiredArgsConstructor
public class VotingSessionController {
  private final VotingSessionService votingSessionService;
  private final ObjectMapper objectMapper;

  @PostMapping("create")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> createVotingSession(@RequestBody VotingSessionRequest votingSessionRequest) {
    try {
      VotingSession votingSession = votingSessionService.createVotingSession(votingSessionRequest.startingAt, votingSessionRequest.endingAt);
      VotingSessionResponse votingSessionResponse = objectMapper.convertValue(votingSession, VotingSessionResponse.class);

      return ResponseEntity.ok(votingSessionResponse);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("{id}/release-private-key")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> releaseVotingSessionPrivateKey(@PathVariable("id") Long votingSessionId) {
    try {
      votingSessionService.releaseVotingSessionPrivateKey(votingSessionId);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("{id}/update-status")
  @PreAuthorize("hasRole('ADMIN')")
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
  @PreAuthorize("hasRole('ADMIN')")
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
      votingSessionService.getVotingSession();
      return ResponseEntity.ok().build();
    } catch (VotingSessionNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
