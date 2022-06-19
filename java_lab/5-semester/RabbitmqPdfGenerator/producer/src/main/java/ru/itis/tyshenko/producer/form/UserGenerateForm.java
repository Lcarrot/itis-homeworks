package ru.itis.tyshenko.producer.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGenerateForm implements Body {

  private String firstName;
  private String lastName;

  @Override
  public String toString() {
    return "{" +
           "firstName=" + firstName +
           ",lastName=" + lastName +
           '}';
  }
}
