package com.gpoalelungi.licenta.repository;

import com.gpoalelungi.licenta.model.VotingSession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {
  @Query(value = "SELECT last_value FROM voting_session_id_seq", nativeQuery = true)
  Long getCurrentVal();

  Optional<VotingSession> findFirstByVotingSessionPublicKeyNotNull();
}
