package ru.itis.tyshenko.producer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.tyshenko.producer.dto.AccessTokenDto;
import ru.itis.tyshenko.producer.dto.RefreshTokenDto;
import ru.itis.tyshenko.producer.dto.SecurityDto;
import ru.itis.tyshenko.producer.form.LoginForm;
import ru.itis.tyshenko.producer.form.RegisterUserForm;
import ru.itis.tyshenko.producer.service.SecurityService;

@RestController
public class SecurityController {

  @Autowired
  private SecurityService securityService;

  @PostMapping("/registry")
  public ResponseEntity<SecurityDto> registry(@RequestBody RegisterUserForm userForm) {
    return securityService.registry(userForm).map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
  }

  @PostMapping("/login")
  public ResponseEntity<SecurityDto> login(@RequestBody LoginForm loginForm) {
    return securityService.login(loginForm)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }

  @PostMapping("/update/updateRefreshToken")
  public ResponseEntity<RefreshTokenDto> updateRefreshToken(@RequestHeader("REFRESH_TOKEN") String refreshToken) {
    return securityService.updateRefreshToken(refreshToken)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.internalServerError().build());
  }

  @PostMapping("/update/updateAccessToken")
  public ResponseEntity<AccessTokenDto> updateAccessToken(@RequestHeader("REFRESH_TOKEN") String refreshToken) {
    return securityService.updateAccessToken(refreshToken)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.internalServerError().build());
  }
}
