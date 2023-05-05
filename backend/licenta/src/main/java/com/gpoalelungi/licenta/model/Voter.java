package com.gpoalelungi.licenta.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

@Entity
@Table(name = "voter")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Voter {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voter_id_seq")
  @SequenceGenerator(name = "voter_id_seq", sequenceName = "voter_id_seq", allocationSize = 1)
  private Long id;

  @Column(name = "public_key", nullable = false, unique = true)
  private String publicKey;

  @Column(name = "private_key", unique = true, nullable = false)
  private String privateKey;

  @Column(name = "created_at", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdAt;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @JsonIgnore
  private User user;

  @Column(name = "is_registered", nullable = false)
  private Boolean isRegistered;
}
