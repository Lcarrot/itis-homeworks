package ru.itis.tyshenko.producer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.tyshenko.producer.dto.AccessTokenDto;
import ru.itis.tyshenko.producer.dto.RefreshTokenDto;
import ru.itis.tyshenko.producer.dto.SecurityDto;
import ru.itis.tyshenko.producer.form.LoginForm;
import ru.itis.tyshenko.producer.form.RegisterUserForm;
import ru.itis.tyshenko.producer.model.RefreshToken;
import ru.itis.tyshenko.producer.model.User;
import ru.itis.tyshenko.producer.repository.RefreshTokenRepository;
import ru.itis.tyshenko.producer.repository.UserRepository;
import ru.itis.tyshenko.producer.util.JwtUtils;

@Service
public class SecurityService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtUtils jwtUtils;

  public Optional<SecurityDto> registry(RegisterUserForm userForm) {
    User user = userForm.toEntity();
    user.setHashPassword(passwordEncoder.encode(userForm.getPassword()));
    user = userRepository.save(user);
    return Optional.of(createSecurityDto(
        refreshTokenRepository.save(jwtUtils.createRefreshToken(user.getEmail())),
        jwtUtils.createAccessToken(user)
    ));
  }

  public Optional<SecurityDto> login(LoginForm loginForm) {
    Optional<User> user = userRepository.findByEmail(loginForm.getEmail());
    if (user.isPresent() && passwordEncoder.matches(loginForm.getPassword(),
        user.get().getHashPassword()
    )) {
      refreshTokenRepository.findByUserEmail(user.get().getEmail())
          .ifPresent(token -> refreshTokenRepository
              .delete(token));
      return Optional.of(createSecurityDto(
          refreshTokenRepository.save(jwtUtils.createRefreshToken(user.get().getEmail())),
          jwtUtils.createAccessToken(user.get())
      ));
    }
    return Optional.empty();
  }

  public Optional<RefreshTokenDto> updateRefreshToken(String token) {
    Optional<RefreshToken> refreshToken = refreshTokenRepository.findByRefreshToken(token);
    if (refreshToken.isPresent()) {
      String userEmail = refreshToken.get().getUserEmail();
      refreshTokenRepository.delete(refreshToken.get());
      return Optional.of(RefreshTokenDto
          .from(
              refreshTokenRepository.save(
                  jwtUtils.createRefreshToken(userEmail))));
    }
    return Optional.empty();
  }

  public Optional<RefreshTokenDto> getRefreshToken(String token) {
    return refreshTokenRepository.findByRefreshToken(token).map(RefreshTokenDto::from);
  }

  public Optional<AccessTokenDto> updateAccessToken(String refreshToken) {
    Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByRefreshToken(
        refreshToken);
    if (refreshTokenOptional.isPresent()) {
      Optional<User> user = userRepository.findByEmail(refreshTokenOptional.get().getUserEmail());
      if (user.isPresent()) {
        return Optional.of(jwtUtils.createAccessToken(user.get()));
      }
    }
    return Optional.empty();
  }

  private SecurityDto createSecurityDto(RefreshToken refreshToken, AccessTokenDto accessTokenDto) {
    return SecurityDto.builder()
        .refreshToken(refreshToken.getRefreshToken())
        .refreshTokenExpiredTime(refreshToken.getExpiredTime())
        .accessToken(accessTokenDto.getJwt())
        .accessTokenExpiredTime(accessTokenDto.getExpiredTime())
        .build();
  }
}
