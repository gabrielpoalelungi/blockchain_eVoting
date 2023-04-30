package com.gpoalelungi.licenta.dto;

import com.gpoalelungi.licenta.model.VotingSessionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VotingSessionResponse {
  private String votingSessionPublicKey;
  private String releasePrivateKey;
  private Date createdAt;
  private Date updatedAt;
  private Date startingAt;
  private Date endingAt;
  private VotingSessionStatus votingSessionStatus;
}
