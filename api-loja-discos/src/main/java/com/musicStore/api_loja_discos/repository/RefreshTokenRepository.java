package com.musicStore.api_loja_discos.repository;

import com.musicStore.api_loja_discos.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserIdAndDevice(Long userId, String device);

    Optional<RefreshToken> findByUserId(Long id);

    @Transactional
    void deleteByExpiryDateBefore(Instant now);
    Optional<RefreshToken> findByToken(String token);

}
