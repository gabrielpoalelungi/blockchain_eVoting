package com.gpoalelungi.licenta.controller;

import com.gpoalelungi.licenta.dto.CandidateRequest;
import com.gpoalelungi.licenta.dto.CandidateResponse;
import com.gpoalelungi.licenta.exceptions.CandidateNotFoundException;
import com.gpoalelungi.licenta.model.Candidate;
import com.gpoalelungi.licenta.service.CandidateService;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
public class CandidateController {
  private final CandidateService candidateService;
  private final ModelMapper modelMapper;

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("add")
  public ResponseEntity<?> addCandidate(@RequestBody CandidateRequest candidateRequest) {
    Candidate newCandidate = modelMapper.map(candidateRequest, Candidate.class);
    try {
      Candidate savedCandidate = candidateService.addCandidate(newCandidate);
      if (savedCandidate != null) {
        CandidateResponse candidateResponse = modelMapper.map(savedCandidate, CandidateResponse.class);
        return ResponseEntity.ok(candidateResponse);
      } else {
        return ResponseEntity.badRequest().body(null);
      }
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("{id}")
  public ResponseEntity<CandidateResponse> getCandidateById(@PathVariable("id") Long candidateId) {
    try {
      Candidate foundCandidate = candidateService.getCandidateById(candidateId);
      CandidateResponse candidateResponse = modelMapper.map(foundCandidate, CandidateResponse.class);
      return ResponseEntity.ok(candidateResponse);
    } catch (CandidateNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping
  public List<CandidateResponse> getAllCandidates() {

    return candidateService.getAllCandidates()
        .stream()
        .map(candidate -> modelMapper.map(candidate, CandidateResponse.class))
        .collect(Collectors.toList());
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteCandidate(@PathVariable(name = "id") Long id) {
    try {
      candidateService.deleteCandidateById(id);
      return ResponseEntity.ok("Candidate deleted successfully");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("{id}")
  public ResponseEntity<?> updateCandidate(@PathVariable long id, @RequestBody CandidateRequest candidateRequest) {

    Candidate candidateRequestFromDto = modelMapper.map(candidateRequest, Candidate.class);

    try {
      Candidate updatedCandidate = candidateService.updateCandidate(id, candidateRequestFromDto);
      CandidateResponse candidateResponse = modelMapper.map(updatedCandidate, CandidateResponse.class);
      return ResponseEntity.ok().body(candidateResponse);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
