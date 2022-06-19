package ru.itis.tyshenko.producer.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.tyshenko.producer.model.Role;
import ru.itis.tyshenko.producer.model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserForm {

  private String firstName;
  private String lastName;
  private String email;
  private Integer age;
  private String password;

  public User toEntity() {
    return User.builder()
        .firstName(firstName)
        .lastName(lastName)
        .email(email)
        .age(age)
        .role(Role.USER)
        .build();
  }
}
