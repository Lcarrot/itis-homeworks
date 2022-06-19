package ru.itis.tyshenko.producer.security.token.refresh;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    RefreshTokenAuthentication auth = (RefreshTokenAuthentication) authentication;
    auth.setAuthenticated(true);
    return auth;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return RefreshTokenAuthentication.class.equals(authentication);
  }
}
