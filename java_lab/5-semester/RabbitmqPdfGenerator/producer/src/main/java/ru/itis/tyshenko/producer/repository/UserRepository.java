package ru.itis.tyshenko.producer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.tyshenko.producer.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
