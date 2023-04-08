package com.gpoalelungi.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

@Entity
@Table(name = "voting_session")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VotingSession {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voting_session_id_seq")
  @SequenceGenerator(name = "voting_session_id_seq", sequenceName = "voting_session_id_seq", allocationSize = 1)
  private Long id;

  @Column(name = "voting_session_public_key", nullable = false, unique = true)
  private String votingSessionPublicKey;

  @Column(name = "released_private_key", unique = true)
  private String releasePrivateKey;

  @Column(name = "created_at", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  private Date updatedAt;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private VotingSessionStatus votingSessionStatus;
}
