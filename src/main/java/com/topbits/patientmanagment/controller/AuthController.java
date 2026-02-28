package com.topbits.patientmanagment.controller;

import com.topbits.patientmanagment.api.dto.request.auth.LoginRequest;
import com.topbits.patientmanagment.api.dto.request.auth.RefreshRequest;
import com.topbits.patientmanagment.api.dto.request.auth.RegisterUserRequest;
import com.topbits.patientmanagment.api.dto.response.LoginResponse;
import com.topbits.patientmanagment.api.dto.response.UserResponse;
import com.topbits.patientmanagment.entity.User;
import com.topbits.patientmanagment.security.JwtService;
import com.topbits.patientmanagment.security.RefreshTokenService;
import com.topbits.patientmanagment.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    final private AuthenticationService authenticationService;
    final private JwtService jwtService;
    final private  RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationService authenticationService,
                          JwtService jwtService,
                           RefreshTokenService refreshTokenService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication){
        Map<String,Object> res =new HashMap<>();
        res.put("email",authentication.getName());
        res.put("roles",authentication.getAuthorities());
        res.put("authenticated", authentication.isAuthenticated());
        return ResponseEntity.ok(res);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        UserDetails userDetails = authenticationService.authenticate(request);

        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = refreshTokenService.issue(userDetails.getUsername());

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getExpirationTime()).build();


        return ResponseEntity.ok(loginResponse);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid RegisterUserRequest request) {
        var user = authenticationService.signup(request);
        UserResponse userResponse=UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getName())
                .build();
        return ResponseEntity.ok(userResponse);
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody @Valid RefreshRequest request) {
        String id = refreshTokenService.getIdOrThrow(request.getRefreshToken());
        LoginResponse user = jwtService.generateTokenFromUserId(id);
        user.setRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(user);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody @Valid RefreshRequest request) {
        refreshTokenService.revoke(request.getRefreshToken());
        return ResponseEntity.ok().build();
    }

}
