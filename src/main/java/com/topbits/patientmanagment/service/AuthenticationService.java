package com.topbits.patientmanagment.service;

import com.topbits.patientmanagment.api.dto.request.auth.LoginRequest;
import com.topbits.patientmanagment.api.dto.request.auth.RegisterUserRequest;
import com.topbits.patientmanagment.common.exception.ConflictException;
import com.topbits.patientmanagment.common.exception.NotFoundException;
import com.topbits.patientmanagment.common.exception.UnauthorizedException;
import com.topbits.patientmanagment.entity.User;
import com.topbits.patientmanagment.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .enabled(true)
                .password(passwordEncoder.encode(request.getPassword())).build();

        return userRepository.save(user);
    }

    public UserDetails authenticate(LoginRequest request) {
        User user=userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("User not found"));
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(String.valueOf(user.getId()), request.password())
            );
            return (UserDetails) auth.getPrincipal();

        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("Invalid email or password");
        }

    }
}
