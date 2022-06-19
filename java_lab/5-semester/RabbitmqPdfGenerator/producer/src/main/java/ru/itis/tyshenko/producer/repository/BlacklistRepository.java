package ru.itis.tyshenko.producer.repository;

import java.util.Optional;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import ru.itis.tyshenko.producer.model.BannedAccessToken;

public interface BlacklistRepository extends KeyValueRepository<BannedAccessToken, String> {

  Optional<BannedAccessToken> findBlacklistAccessTokenByAccessToken(String accessToken);
}
