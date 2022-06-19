package ru.itis.tyshenko.producer.service;


import java.util.Optional;

import ru.itis.tyshenko.producer.dto.UserDto;
import ru.itis.tyshenko.producer.model.User;

public interface UserService {
  Optional<UserDto> getById(Long id);

  Optional<UserDto> save(User user);

  Optional<UserDto> delete(Long id);

  Optional<UserDto> update(Long id, User user);
}
