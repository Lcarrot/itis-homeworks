package ru.itis.tyshenko.simpleconsumer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGenerateForm {

  private String firstName;
  private String lastName;
}
