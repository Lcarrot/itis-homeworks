package ru.itis.tyshenko.producer.dto;

import lombok.Builder;
import lombok.Data;
import ru.itis.tyshenko.producer.model.Role;
import ru.itis.tyshenko.producer.model.User;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
public class UserDto {

  private String firstName;
  private String lastName;
  private String email;
  private Integer age;
  @Enumerated(EnumType.STRING)
  private Role role;

  public static UserDto from(User user) {
    return UserDto.builder()
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .age(user.getAge())
        .role(user.getRole()).build();
  }
}
