package com.gpoalelungi.licenta.repository;

import com.gpoalelungi.licenta.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
  Optional<Candidate> findByOfficialName(String officialName);
}
