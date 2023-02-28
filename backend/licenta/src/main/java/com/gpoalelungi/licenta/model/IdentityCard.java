package com.gpoalelungi.licenta.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "identity_cards")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class IdentityCard {

    @Id
    @Column(name = "identity_card_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "identity_cards_id_seq")
    @SequenceGenerator(name = "identity_cards_id_seq", sequenceName = "identity_cards_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "citizenship", nullable = false)
    @Enumerated(EnumType.STRING)
    Citizenship citizenship;

    @Pattern(regexp = "[1-25-6][0-9][0-9][0-1][1-9][0-3][0-9][0-9][0-9][0-9][0-9][0-9][0-9]", message = "CNP bad formatted")
    @Column(name = "cnp", nullable = false)
    String CNP;

    @Pattern(regexp = "[A-Z][A-Z][0-9][0-9][0-9][0-9][0-9][0-9]", message = "ID Card Number bad formatted")
    @Column(name = "id_card_number", nullable = false)
    String idCardNumber;

    @Column(name = "expiration_date", nullable = false)
    @Temporal(TemporalType.DATE)
    Date expirationDate;

    @OneToOne(mappedBy = "identityCard")
    private User user;

    enum Citizenship {
        ROU,
        ANY
    }
}


