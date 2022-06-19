package itis.Tyshenko.repositories.posts;

import itis.Tyshenko.entity.Ad;
import itis.Tyshenko.repositories.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AdRepository extends CrudRepository<Ad> {

    List<Ad> getAllByUserID(Long userID);
    Optional<Ad> getById(Long id);

    Optional<List<Ad>> getByResumeID(Long resumeID);
}
