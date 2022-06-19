package ru.itis.tyshenko.producer.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import ru.itis.tyshenko.producer.security.token.TokenLogoutFilter;
import ru.itis.tyshenko.producer.security.token.access.AccessTokenAuthFilter;
import ru.itis.tyshenko.producer.security.token.access.AccessTokenAuthenticationProvider;
import ru.itis.tyshenko.producer.security.token.refresh.RefreshTokenAuthFilter;
import ru.itis.tyshenko.producer.security.token.refresh.RefreshTokenAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private AccessTokenAuthFilter accessTokenAuthFilter;

  @Autowired
  private RefreshTokenAuthFilter refreshTokenAuthFilter;

  @Autowired
  private TokenLogoutFilter tokenLogoutFilter;

  @Autowired
  private AccessTokenAuthenticationProvider accessTokenAuthenticationProvider;

  @Autowired
  private RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .sessionManagement()
        .disable()
        .addFilterAt(accessTokenAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(refreshTokenAuthFilter, AccessTokenAuthFilter.class)
        .addFilterAt(tokenLogoutFilter, LogoutFilter.class)
        .authorizeRequests()
        .antMatchers("/login/**")
        .permitAll()
        .antMatchers("/registry")
        .permitAll()
        .antMatchers("/generate/**")
        .authenticated()
        .antMatchers("/metadata/**")
        .authenticated()
        .antMatchers("/update/**")
        .authenticated()
        .antMatchers("/logout")
        .hasAnyAuthority();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(accessTokenAuthenticationProvider)
        .authenticationProvider(refreshTokenAuthenticationProvider);
  }
}
