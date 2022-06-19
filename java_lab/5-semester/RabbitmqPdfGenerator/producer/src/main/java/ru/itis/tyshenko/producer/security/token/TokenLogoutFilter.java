package ru.itis.tyshenko.producer.security.token;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.tyshenko.producer.service.BlacklistService;

@Component
public class TokenLogoutFilter extends OncePerRequestFilter {

  private final String ACCESS_TOKEN_HEADER = "ACCESS-TOKEN";
  private final String REFRESH_TOKEN_HEADER = "REFRESH_TOKEN";

  @Autowired
  private BlacklistService blacklistService;

  private final RequestMatcher logoutRequest = new AntPathRequestMatcher("/logout", "GET");

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (logoutRequest.matches(request)) {
      String access_token = request.getHeader(ACCESS_TOKEN_HEADER);
      String refresh_token = request.getHeader(REFRESH_TOKEN_HEADER);
      blacklistService.banToken(access_token, refresh_token);
      SecurityContextHolder.clearContext();
      return;
    }
    filterChain.doFilter(request, response);
  }
}
