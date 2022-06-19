package ru.itis.tyshenko.producer.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.tyshenko.producer.model.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenDto {

  private String email;
  private Role role;
}
