package ru.itis.tyshenko.producer.form;

import lombok.Builder;
import lombok.Data;
import ru.itis.tyshenko.producer.model.Role;

@Data
@Builder
public class TokenUser {

    private String email;
    private Role role;
    private long createTokenTime;
}
