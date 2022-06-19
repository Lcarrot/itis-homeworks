package itis.Tyshenko.repositories.users;

import itis.Tyshenko.entity.User;
import itis.Tyshenko.repositories.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User> {

    Optional<User> getByLogin(String login);
    Optional<User> getById(Long id);
}
