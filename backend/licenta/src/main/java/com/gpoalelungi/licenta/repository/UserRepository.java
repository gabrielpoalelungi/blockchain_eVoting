package com.gpoalelungi.licenta.repository;

import com.gpoalelungi.licenta.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findById(Long userId);
  Optional<User> findByEmail(String email);
  Optional<User> findByPhoneNumber(String phoneNumber);
  Optional<User> findByHashedIdentityCard(String hashedIdentityCard);
}
