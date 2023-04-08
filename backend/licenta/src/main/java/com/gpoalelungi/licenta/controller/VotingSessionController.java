package com.gpoalelungi.licenta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpoalelungi.licenta.dto.UserResponse;
import com.gpoalelungi.licenta.dto.VotingSessionResponse;
import com.gpoalelungi.licenta.exceptions.UserNotFoundException;
import com.gpoalelungi.licenta.model.User;
import com.gpoalelungi.licenta.model.VotingSession;
import com.gpoalelungi.licenta.service.VotingSessionService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voting-session")
@RequiredArgsConstructor
public class VotingSessionController {

  private final VotingSessionService votingSessionService;

  private final ObjectMapper objectMapper;

  @GetMapping("create")
  public ResponseEntity<VotingSessionResponse> createVotingSession() {
    try {
      VotingSession votingSession = votingSessionService.createVotingSession();
      VotingSessionResponse votingSessionResponse = objectMapper.convertValue(votingSession, VotingSessionResponse.class);

      return ResponseEntity.ok(votingSessionResponse);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
}
