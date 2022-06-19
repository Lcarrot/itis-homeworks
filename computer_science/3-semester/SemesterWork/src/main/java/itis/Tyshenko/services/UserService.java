package itis.Tyshenko.services;
import itis.Tyshenko.dto.UserDTO;
import itis.Tyshenko.entity.User;

import java.util.Optional;


public interface UserService {

    Optional<UserDTO> getByLogin(String login);

    void add(UserDTO entity, String password);
    Optional<UserDTO> getById(Long id);
    boolean equalsRowPasswordWithUserPassword(String userHashedPassword, String password);
    void update(UserDTO entity, String password);
}
