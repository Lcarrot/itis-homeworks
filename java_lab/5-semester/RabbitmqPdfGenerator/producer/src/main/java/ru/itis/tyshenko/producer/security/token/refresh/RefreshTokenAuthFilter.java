package ru.itis.tyshenko.producer.security.token.refresh;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.tyshenko.producer.dto.RefreshTokenDto;
import ru.itis.tyshenko.producer.service.SecurityService;

@Component
@Log
public class RefreshTokenAuthFilter extends OncePerRequestFilter {

  @Autowired
  private SecurityService securityService;

  private final String REFRESH_TOKEN_HEADER = "REFRESH_TOKEN";

  private final RequestMatcher refreshAccessToken = new AntPathRequestMatcher(
      "/update/updateAccessToken", "POST");

  private final RequestMatcher refreshRefreshToken = new AntPathRequestMatcher(
      "/update/updateRefreshToken", "POST");

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (refreshAccessToken.matches(request) || refreshRefreshToken.matches(request)) {
      Optional<RefreshTokenDto> token = securityService.getRefreshToken(
          request.getHeader(REFRESH_TOKEN_HEADER));
      if (token.isPresent()) {
        if (refreshAccessToken.matches(request)
            && token.get().getExpiredTime().compareTo(new Date(System.currentTimeMillis())) < 0) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.getWriter().write("refresh token expired \n");
          return;
        }
        else {
          RefreshTokenAuthentication authentication = new RefreshTokenAuthentication(
              token.get());
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    }
    filterChain.doFilter(request, response);
  }
}
