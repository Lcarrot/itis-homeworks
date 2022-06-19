package ru.itis.Tyshenko.services;

import ru.itis.Tyshenko.entity.Cottage;

import java.util.Optional;

public interface CottageService {

    Optional<Cottage> get(Cottage cottage);

    void add(Cottage cottage);

    void update(Cottage cottage);

    void delete(Cottage cottage);
}
