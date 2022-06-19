package ru.itis.tyshenko.producer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.tyshenko.producer.dto.UserDto;
import ru.itis.tyshenko.producer.model.User;
import ru.itis.tyshenko.producer.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public Optional<UserDto> getById(Long id) {
    return userRepository.findById(id).map(UserDto::from);
  }

  @Override
  public Optional<UserDto> save(User user) {
    return Optional.of(userRepository.save(user)).map(UserDto::from);
  }

  @Override
  public Optional<UserDto> delete(Long id) {
    Optional<User> user = userRepository.findById(id);
    user.ifPresent(value -> userRepository.delete(value));
    return user.map(UserDto::from);
  }

  @Override
  public Optional<UserDto> update(Long id, User user) {
    Optional<User> dbUser = userRepository.findById(id);
    dbUser.ifPresent((user1 -> {
      user1.setId(id);
      userRepository.save(user);
    }));
    return dbUser.map(UserDto::from);
  }
}
