package ru.itis.tyshenko.producer.repository;

import java.util.Optional;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import ru.itis.tyshenko.producer.model.RefreshToken;

public interface RefreshTokenRepository extends KeyValueRepository<RefreshToken, String> {

    Optional<RefreshToken> findByRefreshToken(String token);

    Optional<RefreshToken> findByUserEmail(String email);
}
