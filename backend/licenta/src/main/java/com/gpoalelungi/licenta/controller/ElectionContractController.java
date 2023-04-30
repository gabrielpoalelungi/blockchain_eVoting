package com.gpoalelungi.licenta.controller;

import com.gpoalelungi.licenta.contract.ElectionContractService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/election-contract")
@RequiredArgsConstructor
public class ElectionContractController {
  private final ElectionContractService electionContractService;

  @GetMapping("/is-vote-started")
  public ResponseEntity<Boolean> isVoteStarted() throws Exception {
    return  ResponseEntity.ok(electionContractService.isVoteStarted());
  }

  @GetMapping("/is-vote-finished")
  public ResponseEntity<Boolean> isVoteFinished() throws Exception {
    return  ResponseEntity.ok(electionContractService.isVoteFinished());
  }

  @GetMapping("/start-vote")
  public ResponseEntity<String> doStartVote() throws Exception {
    return ResponseEntity.ok(electionContractService.startVote());
  }

  @GetMapping("/end-vote")
  public ResponseEntity<String> doEndVote() throws Exception {
    return ResponseEntity.ok(electionContractService.endVote().toString());
  }
}
