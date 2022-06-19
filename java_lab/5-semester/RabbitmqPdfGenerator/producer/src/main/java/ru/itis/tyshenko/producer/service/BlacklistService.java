package ru.itis.tyshenko.producer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.tyshenko.producer.model.BannedAccessToken;
import ru.itis.tyshenko.producer.repository.BlacklistRepository;
import ru.itis.tyshenko.producer.repository.RefreshTokenRepository;

@Service
public class BlacklistService {

  @Autowired
  private BlacklistRepository blacklistRepository;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  public void banToken(String accessToken, String refreshToken) {
    blacklistRepository.save(
        BannedAccessToken.builder()
            .accessToken(accessToken)
            .build());
    refreshTokenRepository.delete(refreshTokenRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new IllegalStateException("refresh token isn't found")));
  }

  public Optional<String> findBannedToken(String accessToken) {
    return blacklistRepository.findBlacklistAccessTokenByAccessToken(accessToken)
        .map(BannedAccessToken::getAccessToken);
  }
}
