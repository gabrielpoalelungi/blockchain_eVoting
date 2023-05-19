package com.gpoalelungi.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

@Entity
@Table(name = "candidate")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Candidate {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidate_id_seq")
  @SequenceGenerator(name = "candidate_id_seq", sequenceName = "candidate_id_seq", allocationSize = 1)
  private Long id;

  @Column(name = "official_name", nullable = false, unique = true)
  private String officialName;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "created_at", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdAt;

  @Column(name = "updated_at", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  private Date updatedAt;

  @Column(name = "number_of_votes")
  private Long numberOfVotes;
}
