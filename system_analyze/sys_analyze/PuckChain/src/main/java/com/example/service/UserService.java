package com.example.service;

import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.form.UserForm;
import com.example.repository.UserRepository;
import com.example.security.BouncyCastleSecurityKey;
import io.micronaut.core.convert.ConversionService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class UserService {

    @Inject
    private BouncyCastleSecurityKey keyGenerator;
    @Inject
    private UserRepository userRepository;
    @Inject
    ConversionService<?> conversionService;

    public UserDto createUser(UserForm userForm) {
        var keyPair = keyGenerator.generateKeys();
        var user = User.builder().privateKey(keyPair.getPrivate()).name(userForm.getName()).build();
        user = userRepository.save(user);
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .publicKey(conversionService.convert(keyPair.getPublic(), String.class).orElseThrow())
                .build();
    }
}
