package com.gpoalelungi.licenta.service;

import com.gpoalelungi.licenta.contract.Election;
import com.gpoalelungi.licenta.exceptions.CandidateException;
import com.gpoalelungi.licenta.exceptions.CandidateNotFoundException;
import com.gpoalelungi.licenta.exceptions.VotingSessionNotFinishedException;
import com.gpoalelungi.licenta.model.Candidate;
import com.gpoalelungi.licenta.model.VotingSession;
import com.gpoalelungi.licenta.model.VotingSessionStatus;
import com.gpoalelungi.licenta.repository.CandidateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandidateService {
  private final CandidateRepository candidateRepository;

  private final VotingSessionService votingSessionService;

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
    VotingSession votingSession = votingSessionService.getVotingSession();
    if (votingSession.getVotingSessionStatus() != VotingSessionStatus.NOT_STARTED) {
      throw new CandidateException("Cannot delete candidate. Voting session already started.");
    }

    log.info("Deleting candidate with id: {}", candidateId);
    candidateRepository.deleteById(candidateId);
  }

  public Candidate updateCandidate(Long candidateId, Candidate candidateRequest) {
    VotingSession votingSession = votingSessionService.getVotingSession();
    if (votingSession.getVotingSessionStatus() != VotingSessionStatus.NOT_STARTED) {
      throw new CandidateException("Cannot delete candidate. Voting session already started.");
    }

    log.info("Updating candidate with id: {}", candidateId);
    Candidate candidateToBeUpdated = candidateRepository.findById(candidateId)
        .orElseThrow(() -> new CandidateNotFoundException("Candidate not found"));

    candidateToBeUpdated.setOfficialName(candidateRequest.getOfficialName());
    candidateToBeUpdated.setDescription(candidateRequest.getDescription());
    candidateToBeUpdated.setPhotoUrl(candidateRequest.getPhotoUrl());

    return candidateRepository.save(candidateToBeUpdated);
  }

  private void incrementVoteCount(Long candidateId) {
    Candidate candidate = getCandidateById(candidateId);
    if (candidate.getNumberOfVotes() == null) {
        candidate.setNumberOfVotes(1L);
    } else {
        candidate.setNumberOfVotes(candidate.getNumberOfVotes() + 1);
    }
    candidateRepository.save(candidate);
  }

  public void countVotesForCandidates() throws Exception {
    VotingSession votingSession = votingSessionService.getVotingSession();

    if (votingSession.getVotingSessionStatus() != VotingSessionStatus.FINISHED) {
      throw new VotingSessionNotFinishedException("Cannot count votes. Voting session NOT FINISHED yet.");
    }

    List<Candidate> candidates = getAllCandidates();
    List<Election.Vote> votes = votingSessionService.getAllVotes();
    for (Election.Vote vote : votes) {
      String choice = votingSessionService.decryptVote(vote.publicKey, vote.encryptedVote, vote.signature);
      for (Candidate candidate : candidates) {
        if (candidate.getOfficialName().toLowerCase(Locale.ROOT).equals(choice.toLowerCase(Locale.ROOT))) {
          incrementVoteCount(candidate.getId());
        }
      }
    }
  }

  public Long countTotalVotes() {
    VotingSession votingSession = votingSessionService.getVotingSession();

    if (votingSession.getVotingSessionStatus() != VotingSessionStatus.FINISHED) {
      throw new VotingSessionNotFinishedException("Cannot count votes. Voting session NOT FINISHED yet.");
    }

    List<Candidate> candidates = getAllCandidates();
    Long totalVotes = 0L;

    for(Candidate candidate : candidates) {
      if (candidate.getNumberOfVotes() != null) {
        totalVotes += candidate.getNumberOfVotes();
      }
    }
    return totalVotes;
  }
}
