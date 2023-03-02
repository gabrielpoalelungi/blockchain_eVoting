package com.gpoalelungi.licenta.service;

import com.gpoalelungi.licenta.exceptions.UserAlreadyExistsException;
import com.gpoalelungi.licenta.exceptions.UserNotFoundException;
import com.gpoalelungi.licenta.model.User;
import com.gpoalelungi.licenta.model.UserDetailsResponse;
import com.gpoalelungi.licenta.model.Voter;
import com.gpoalelungi.licenta.repository.UserRepository;
import com.gpoalelungi.licenta.repository.VoterRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean registerUserHasVoted() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = ((User) auth.getPrincipal()).getEmail();
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UserNotFoundException("User with this email was not found"));

        if (user.getVoter().getHasVoted() == Boolean.FALSE) {
            user.getVoter().setHasVoted(Boolean.TRUE);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public UserDetailsResponse getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = ((User) auth.getPrincipal()).getEmail();
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UserNotFoundException("User with this email was not found"));

        return UserDetailsResponse.builder()
            .userEmail(user.getEmail())
            .cnp(user.getIdentityCard().getCNP())
            .idCardNumber(user.getIdentityCard().getIdCardNumber())
            .votingPublicKey(user.getVoter().getVoterPublicKey())
            .hasVoted(user.getVoter().getHasVoted())
            .build();
    }
}
