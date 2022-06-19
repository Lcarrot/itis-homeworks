package ru.itis.tyshenko.producer.security.token.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.itis.tyshenko.producer.dto.UserTokenDto;
import ru.itis.tyshenko.producer.service.BlacklistService;
import ru.itis.tyshenko.producer.util.JwtUtils;

@Component
public class AccessTokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    @Qualifier("CustomUserDetailsService")
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AccessTokenAuthentication accessTokenAuthentication = (AccessTokenAuthentication) authentication;
        UserTokenDto userTokenDto;
        try {
            userTokenDto = jwtUtils.getUserToken(authentication.getName());
        } catch (RuntimeException e) {
            throw new BadCredentialsException("Access token is invalid");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(userTokenDto.getEmail());
        accessTokenAuthentication.setAuthenticated(
            blacklistService.findBannedToken(accessTokenAuthentication.getName()).isPresent());
        accessTokenAuthentication.setUserDetails(userDetails);
        return accessTokenAuthentication;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return AccessTokenAuthentication.class.equals(aClass);
    }
}
