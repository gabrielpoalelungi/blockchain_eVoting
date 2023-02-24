package com.gpoalelungi.licenta.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.PublicKey;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "voters")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Voter {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voters_id_seq")
    @SequenceGenerator(name = "voters_id_seq", sequenceName = "voters_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "public_key", nullable = false)
    private String voterPublicKey;

    @Column(name = "has_voted")
    private Boolean hasVoted;

}
