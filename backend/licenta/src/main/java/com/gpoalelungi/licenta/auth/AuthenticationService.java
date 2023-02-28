package com.gpoalelungi.licenta.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpoalelungi.licenta.config.JwtService;
import com.gpoalelungi.licenta.exceptions.UserAlreadyExistsException;
import com.gpoalelungi.licenta.model.IdentityCard;
import com.gpoalelungi.licenta.model.Role;
import com.gpoalelungi.licenta.model.User;
import com.gpoalelungi.licenta.model.Voter;
import com.gpoalelungi.licenta.repository.IdentityCardRepository;
import com.gpoalelungi.licenta.repository.UserRepository;
import com.gpoalelungi.licenta.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final VoterRepository voterRepository;
    private final IdentityCardRepository identityCardRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private static ObjectMapper mapper;

    public AuthenticationResponse register(RegisterRequest request) {
        String cnp = request.getIdentityCard().getCNP();

        Voter voter = Voter.builder()
                .voterPublicKey(request.getVoter().getVoterPublicKey())
                .hasVoted(Boolean.FALSE)
                .build();

        IdentityCard identityCard = IdentityCard.builder()
            .citizenship(request.getIdentityCard().getCitizenship())
            .CNP(request.getIdentityCard().getCNP())
            .expirationDate(request.getIdentityCard().getExpirationDate())
            .idCardNumber(request.getIdentityCard().getIdCardNumber())
            .build();

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .voter(voter)
                .identityCard(identityCard)
                .build();

        if (identityCardRepository.findByCNPAndIdCardNumber(identityCard.getCNP(), identityCard.getIdCardNumber()).isPresent()) {
            throw new UserAlreadyExistsException("User already registered! ID number: " + user.getIdentityCard().getIdCardNumber());
        }

        identityCardRepository.save(identityCard);
        voterRepository.save(voter);
        userRepository.save(user);

        //TODO log
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        authenticationManager.authenticate(authToken);

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
