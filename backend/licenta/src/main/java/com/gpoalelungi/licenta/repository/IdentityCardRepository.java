package com.gpoalelungi.licenta.repository;

import com.gpoalelungi.licenta.model.IdentityCard;
import com.gpoalelungi.licenta.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityCardRepository extends JpaRepository<IdentityCard, Long> {
  Optional<IdentityCard> findByCNPAndIdCardNumber(String CNP, String idCardNumber);

}
