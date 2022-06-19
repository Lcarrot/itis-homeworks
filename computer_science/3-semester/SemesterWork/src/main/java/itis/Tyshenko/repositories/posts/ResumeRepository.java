package itis.Tyshenko.repositories.posts;

import itis.Tyshenko.entity.Resume;
import itis.Tyshenko.repositories.CrudRepository;

import java.util.Optional;

public interface ResumeRepository extends CrudRepository<Resume> {

    Optional<Resume> findByUserId(Long userId);
}
