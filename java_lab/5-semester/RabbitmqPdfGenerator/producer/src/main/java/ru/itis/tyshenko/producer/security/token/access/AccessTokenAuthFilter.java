package ru.itis.tyshenko.producer.security.token.access;

import java.io.IOException;
import java.util.logging.Level;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.java.Log;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Log
public class AccessTokenAuthFilter extends OncePerRequestFilter {

  private final String ACCESS_TOKEN_HEADER = "ACCESS-TOKEN";

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, FilterChain filterChain)
      throws ServletException, IOException {
    String token = httpServletRequest.getHeader(ACCESS_TOKEN_HEADER);
    if (token != null) {
      AccessTokenAuthentication accessTokenAuthentication = new AccessTokenAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(accessTokenAuthentication);
      log.log(Level.INFO, "set auth in context");
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
