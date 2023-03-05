package com.example;

import com.example.form.UserForm;
import com.example.service.UserService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;

@MicronautTest
public class UserTest {

    private static final Logger logger = LoggerFactory.getLogger(UserTest.class);

    @Inject
    UserService userService;

    @Test
    void saveUser() {
        var form = UserForm.builder().name("leo").build();
        logger.info(userService.createUser(form).toString());
    }

    @Test
    void testFormat() {
        System.out.println(DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss'.'SSSX")
                .parse("2022-11-19T18:33:18.910+03"));
    }
}
