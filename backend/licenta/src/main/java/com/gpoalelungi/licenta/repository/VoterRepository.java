package com.gpoalelungi.licenta.repository;

import com.gpoalelungi.licenta.model.Voter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {

  Optional<Voter> findByUser_Id(Long userId);
}
