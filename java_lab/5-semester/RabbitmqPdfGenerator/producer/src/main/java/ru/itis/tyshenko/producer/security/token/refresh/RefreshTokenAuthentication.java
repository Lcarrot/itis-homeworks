package ru.itis.tyshenko.producer.security.token.refresh;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.itis.tyshenko.producer.dto.RefreshTokenDto;

public class RefreshTokenAuthentication implements Authentication {

  private final RefreshTokenDto refreshToken;
  private boolean isAuth;

  public RefreshTokenAuthentication(RefreshTokenDto refreshToken) {
    this.refreshToken = refreshToken;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return null;
  }

  @Override
  public boolean isAuthenticated() {
    return isAuth;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    isAuth = isAuthenticated;
  }

  @Override
  public String getName() {
    return refreshToken.getRefreshToken();
  }
}
