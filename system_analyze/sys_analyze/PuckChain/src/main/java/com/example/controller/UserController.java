package com.example.controller;

import com.example.dto.UserDto;
import com.example.form.UserForm;
import com.example.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

import javax.validation.Valid;
import java.security.PublicKey;

@Controller("/user")
public class UserController {

    @Inject
    private UserService userService;

    @Post("create")
    HttpResponse<UserDto> createUser(@Valid UserForm userForm) {
        return HttpResponse.created(userService.createUser(userForm));
    }
}
