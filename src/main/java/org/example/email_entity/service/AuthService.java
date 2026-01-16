package org.example.email_entity.service;

import lombok.RequiredArgsConstructor;
import org.example.email_entity.dto.AuthResponse;
import org.example.email_entity.dto.EmailBody;
import org.example.email_entity.dto.LoginDto;
import org.example.email_entity.entity.User;
import org.example.email_entity.repository.UserRepository;
import org.example.email_entity.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service

public class AuthService {

    @Autowired
    private final EmailServiceImpl emailService;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtService; // your token generator

    public AuthService(EmailServiceImpl emailService, AuthenticationManager authenticationManager, UserRepository userRepository, JwtUtil jwtService) {
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public String login(LoginDto request) {

        // 1) verifies password using AuthenticationProvider + PasswordEncoder
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2) if authenticate() didn't throw -> user is valid
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3) generate jwt
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        // 4) return token
        EmailBody message = EmailBody.builder()
                .recipient(user.getEmail())
                .subject("------ ACCESS TOKEN ------")
                .body("Name : " + user.getFullName() + "\nemail: " + user.getEmail() + "\nACCESS TOKEN: " + "------   " + token + "   ------")
                .build();

        emailService.sendEmail(message);

        return "Token was sent to the email: " + user.getEmail();
    }
}
