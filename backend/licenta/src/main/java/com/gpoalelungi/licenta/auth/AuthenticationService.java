package com.gpoalelungi.licenta.auth;

import com.gpoalelungi.licenta.config.JwtService;
import com.gpoalelungi.licenta.model.Role;
import com.gpoalelungi.licenta.model.User;
import com.gpoalelungi.licenta.model.Voter;
import com.gpoalelungi.licenta.repository.UserRepository;
import com.gpoalelungi.licenta.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final VoterRepository voterRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Voter voter = Voter.builder()
                .voterPublicKey(request.getVoter().getVoterPublicKey())
                .hasVoted(Boolean.FALSE)
                .build();

        User user = User.builder()
                .email(request.getEmail())
                .identityCard(request.getIdentityCard())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .voter(voter)
                .build();

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
