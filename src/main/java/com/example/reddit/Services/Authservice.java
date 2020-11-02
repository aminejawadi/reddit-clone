package com.example.reddit.Services;

import com.example.reddit.DTO.AuthenticationResponse;
import com.example.reddit.DTO.LoginRequest;
import com.example.reddit.DTO.RegisterRequest;
import com.example.reddit.Jwt.JwtProvider;
import com.example.reddit.Models.NotificationEmail;
import com.example.reddit.Models.User;
import com.example.reddit.Models.VerificationToken;
import com.example.reddit.Repository.UserRepository;
import com.example.reddit.Repository.VerificationTokenRepository;
import com.example.reddit.exceptions.SpringRedditException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
@Service
@AllArgsConstructor

public class Authservice {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest registerRequest) {

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

        String token = generateverificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your account", user.getEmail(), "welcome you are succssefuly registred :" + "http://localhost:8080/api/auth/accountVerification" + token));
    }

    private String generateverificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }


    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("invalid token"));
        fetchUserAndEnable(verificationToken.get());

    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SecurityException("user doesnt exit"));
        user.setEnabled(true);
        userRepository.save(user);

    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

return  new AuthenticationResponse(token,loginRequest.getUsername());

    }
}