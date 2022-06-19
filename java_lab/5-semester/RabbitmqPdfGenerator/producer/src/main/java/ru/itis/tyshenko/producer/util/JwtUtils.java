package ru.itis.tyshenko.producer.util;

import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itis.tyshenko.producer.dto.AccessTokenDto;
import ru.itis.tyshenko.producer.dto.UserTokenDto;
import ru.itis.tyshenko.producer.model.RefreshToken;
import ru.itis.tyshenko.producer.model.Role;
import ru.itis.tyshenko.producer.model.User;

@Component
public class JwtUtils {

  @Value("${refresh.token.time}")
  private Long refreshTokenAliveTime;

  @Value("${access.token.time}")
  private Long accessTokenAliveTime;

  @Value("${jwt.key}")
  private String signingKey;

  private final String ROLE_CLAIM_NAME = "role";

  public RefreshToken createRefreshToken(String userEmail) {
    return RefreshToken.builder().refreshToken(UUID.randomUUID().toString()).userEmail(userEmail)
        .expiredTime(new Date(System.currentTimeMillis() + refreshTokenAliveTime))
        .build();
  }

  public AccessTokenDto createAccessToken(User user) {
    Date expirationTime = new Date(System.currentTimeMillis() + accessTokenAliveTime);
    String token = new DefaultJwtBuilder()
        .setSubject(user.getEmail())
        .claim(ROLE_CLAIM_NAME, user.getRole().toString())
        .setExpiration(expirationTime)
        .signWith(SignatureAlgorithm.HS256, signingKey).compact();
    return AccessTokenDto.builder().jwt(token).expiredTime(expirationTime).build();
  }

  public UserTokenDto getUserToken(String jwt) {
    Claims claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jwt).getBody();
    return UserTokenDto.builder()
        .email(claims.getSubject())
        .role(Role.valueOf(String.valueOf(claims.get(ROLE_CLAIM_NAME))))
        .build();
  }
}
