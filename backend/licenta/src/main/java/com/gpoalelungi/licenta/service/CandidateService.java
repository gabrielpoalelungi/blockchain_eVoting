package com.gpoalelungi.licenta.service;

import com.gpoalelungi.licenta.exceptions.CandidateNotFoundException;
import com.gpoalelungi.licenta.model.Candidate;
import com.gpoalelungi.licenta.repository.CandidateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandidateService {
  private final CandidateRepository candidateRepository;

  public Candidate addCandidate(Candidate candidate) {
    log.info("Adding candidate: {}", candidate);
    return candidateRepository.save(candidate);
  }

  public Candidate getCandidateById(Long candidateId) {
    log.info("Getting candidate with id: {}", candidateId);
    return candidateRepository.findById(candidateId)
        .orElseThrow(() -> new CandidateNotFoundException("Candidate not found"));
  }

  public List<Candidate> getAllCandidates() {
    log.info("Getting all candidates");
    return candidateRepository.findAll();
  }

  public void deleteCandidateById(Long candidateId) {
    log.info("Deleting candidate with id: {}", candidateId);
    candidateRepository.deleteById(candidateId);
  }

  public Candidate updateCandidate(Long candidateId, Candidate candidateRequest) {
    log.info("Updating candidate with id: {}", candidateId);
    Candidate candidateToBeUpdated = candidateRepository.findById(candidateId)
        .orElseThrow(() -> new CandidateNotFoundException("Candidate not found"));

    candidateToBeUpdated.setOfficialName(candidateRequest.getOfficialName());
    candidateToBeUpdated.setDescription(candidateRequest.getDescription());
    candidateToBeUpdated.setPhotoUrl(candidateRequest.getPhotoUrl());

    return candidateRepository.save(candidateToBeUpdated);
  }
}
