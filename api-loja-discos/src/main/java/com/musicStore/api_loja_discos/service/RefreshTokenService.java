package com.musicStore.api_loja_discos.service;


//nessa classe, vamos criar o refreshToken

import com.musicStore.api_loja_discos.domain.RefreshToken;
import com.musicStore.api_loja_discos.repository.RefreshTokenRepository;
import com.musicStore.api_loja_discos.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

        @Value("${jwt.refreshExpirationMs}")
        private long refreshTokenDurationMs ;
        private final UserRepository userRepository;
        private final RefreshTokenRepository refreshTokenRepository;

        @Transactional
    public RefreshToken createRefreshToken(Long userId, String device) { //this method creates a refresh token and sets it to the user

            Optional<RefreshToken> existedToken = refreshTokenRepository.findByUserIdAndDevice(userId, device);

            if (existedToken.isPresent()) {
                RefreshToken refreshToken = existedToken.get();

                refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
                refreshToken.setToken(UUID.randomUUID().toString());
                refreshToken.setDevice(device);
                return refreshTokenRepository.save(refreshToken);
            } else {
                var token = new RefreshToken();
                token.setUser(userRepository.findById(userId).get());
                token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
                token.setToken(UUID.randomUUID().toString());
                token.setDevice(device);
                return refreshTokenRepository.save(token);
            }


    }

    @Scheduled(fixedRate = 3600000)
    public void invoceDeleteToken() {
            refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

}
