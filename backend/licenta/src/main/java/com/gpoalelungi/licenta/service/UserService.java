package com.gpoalelungi.licenta.service;

import com.gpoalelungi.licenta.exceptions.UserAlreadyExistsException;
import com.gpoalelungi.licenta.model.User;
import com.gpoalelungi.licenta.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private User saveVoterToDatabase(User user) {
        log.info("Saved user with email={}", user.getEmail());
        return userRepository.save(user);
    }

    public User signup(User userToBeSaved) {
        if (userRepository.existsVoterByIdentityCard(userToBeSaved.getIdentityCard())) {
            throw new UserAlreadyExistsException("User already registered");
        } else {
            log.info("Saved voter with email={}", userToBeSaved.getEmail());
            return saveVoterToDatabase(userToBeSaved);
        }
    }
}
