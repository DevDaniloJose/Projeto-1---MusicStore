package com.musicStore.api_loja_discos.controller;


import com.musicStore.api_loja_discos.domain.RefreshToken;
import com.musicStore.api_loja_discos.domain.User;
import com.musicStore.api_loja_discos.repository.RefreshTokenRepository;
import com.musicStore.api_loja_discos.repository.UserRepository;
import com.musicStore.api_loja_discos.requests.AuthRequest;
import com.musicStore.api_loja_discos.requests.AuthResponse;
import com.musicStore.api_loja_discos.requests.LoginDTO;
import com.musicStore.api_loja_discos.service.JWTService;
import com.musicStore.api_loja_discos.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

        private final RefreshTokenRepository refreshTokenRepository;
        private final RefreshTokenService refreshTokenService;
        private final JWTService jwtService;
        private final UserDetailsService userDetailsService;
        @Autowired
        private  AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @Valid
    @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, @RequestHeader("User-Agent") String userAgent) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequest.username(),
                                authRequest.password()
                        )
                );

                String device = "Unknown";

              User userFound =  userRepository.findByUsername(authRequest.username()).orElseThrow(()-> new RuntimeException("User not found"));
                String token = jwtService.generateToken(authRequest.username());
                if (userAgent.contains("Windows") || userAgent.contains("Macintosh")|| userAgent.contains("Postman")) {
                    device = "PC";
                } else if (userAgent.contains("Android") || userAgent.contains("Iphone")) {
                    device = "SmartPhone";
                }


                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userFound.getId(), device);
                AuthResponse responseData = new AuthResponse(token, refreshToken, "Login successful");
                return ResponseEntity.ok(responseData);


            } catch (AuthenticationException e) {
                System.out.println("Manager failed. Reason: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
                    }


            @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> payload) {
        String requestToken = payload.get("refreshToken");

        if (requestToken == null) {
            return ResponseEntity.badRequest().body("Refresh token is required");
        }

        return refreshTokenRepository.findByToken(requestToken)
                .map(token -> {
                    if (refreshTokenService.isTokenExpired(token)) {
                        refreshTokenRepository.delete(token);
                        return ResponseEntity.badRequest().body("Refresh token expired. Please log in again.");
                    }
                    String newJwt = jwtService.generateToken(token.getUser().getUsername());
                    return ResponseEntity.ok(Map.of("token", newJwt));
                }).orElseGet(() -> ResponseEntity.badRequest().body("Invalid refresh token"));

    }





}