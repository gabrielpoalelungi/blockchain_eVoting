package com.gpoalelungi.licenta.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpoalelungi.licenta.config.JwtService;
import com.gpoalelungi.licenta.exceptions.InvalidIdentityCardException;
import com.gpoalelungi.licenta.exceptions.UserAlreadyExistsException;
import com.gpoalelungi.licenta.exceptions.UserNotFoundException;
import com.gpoalelungi.licenta.model.IdentityCard;
import com.gpoalelungi.licenta.model.IdentityCardJsonConverter;
import com.gpoalelungi.licenta.model.Role;
import com.gpoalelungi.licenta.model.User;
import com.gpoalelungi.licenta.model.Voter;
import com.gpoalelungi.licenta.repository.UserRepository;
import com.gpoalelungi.licenta.repository.VoterRepository;
import com.gpoalelungi.licenta.service.IdentityCardService;
import com.gpoalelungi.licenta.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final VoterRepository voterRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IdentityCardJsonConverter identityCardJsonConverter;
    private final IdentityCardService identityCardService;

    public AuthenticationResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.error("User already registered with this email!");
            throw new UserAlreadyExistsException("User already registered with this email!");
        }

        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            log.error("User already registered with this email!");
            throw new UserAlreadyExistsException("User already registered with this phone number!");
        }

        if(!identityCardService.validateIdentityCard(request.getIdentityCard())) {
            log.error("Invalid identity card!");
            throw new InvalidIdentityCardException("Invalid identity card!");
        }

        String hashedIdentityCard = passwordEncoder.encode(identityCardJsonConverter.convertToDatabaseColumn(request.getIdentityCard()));

        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (passwordEncoder.matches(identityCardJsonConverter.convertToDatabaseColumn(request.getIdentityCard()), user.getHashedIdentityCard())) {
                log.error("User already registered with this identity card!");
                throw new UserAlreadyExistsException("User already registered with this identity card!");
            }
        }

        User newUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .hashedIdentityCard(hashedIdentityCard)
                .role(Role.USER)
                .build();

        try {
            KeyPair keyPair = generateKeys();
            Voter newVoter = Voter.builder()
                    .user(newUser)
                    .publicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()))
                    .privateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()))
                    .build();

            userRepository.save(newUser);
            log.info("Saved user={} to database", newUser);

            voterRepository.save(newVoter);
            log.info("Saved voter={} to database", newVoter);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error while generating keys for voter: " + newUser);
            e.printStackTrace();
        }

        String jwtToken = jwtService.generateToken(newUser);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("Successful register!")
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        authenticationManager.authenticate(authToken);

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UserNotFoundException("User not found with email=" + request.getEmail()));

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        String jwtToken = jwtService.generateToken(extraClaims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("Successful login!")
                .build();
    }

    private KeyPair generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // specify key size
        return keyPairGenerator.generateKeyPair();
    }
}
