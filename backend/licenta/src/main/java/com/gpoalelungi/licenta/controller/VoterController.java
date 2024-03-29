package com.gpoalelungi.licenta.controller;

import com.gpoalelungi.licenta.dto.VoterResponse;
import com.gpoalelungi.licenta.exceptions.UserNotFoundException;
import com.gpoalelungi.licenta.model.Voter;
import com.gpoalelungi.licenta.service.VoterService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voters")
@RequiredArgsConstructor
public class VoterController {
  private final VoterService voterService;

  @GetMapping("logged-user")
  public ResponseEntity<VoterResponse> getVoterDetailsOfLoggedUser() {
    try {
      Voter foundVoter = voterService.getVoterDetailsOfLoggedUser();
      VoterResponse voterResponse = VoterResponse.builder()
          .publicKey(foundVoter.getPublicKey())
          .privateKey(foundVoter.getPrivateKey())
          .build();
      return ResponseEntity.ok(voterResponse);
    } catch (UserNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
