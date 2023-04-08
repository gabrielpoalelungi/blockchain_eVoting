package com.gpoalelungi.licenta.repository;

import com.gpoalelungi.licenta.model.VotingSession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {
  @Query(value = "SELECT nextval('voting_session_id_seq')", nativeQuery = true)
  Long getNextVal();
}
