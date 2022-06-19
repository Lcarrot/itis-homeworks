package ru.itis.Tyshenko.repositories;

import ru.itis.Tyshenko.entity.Cottage;

import java.util.Optional;

public interface CottageRepository {

    Optional<Cottage> get(Cottage cottage);
    void add(Cottage cottage);
    void update(Cottage cottage);
    void delete(Cottage cottage);
}
