package ru.itis.Tyshenko.services;

import ru.itis.Tyshenko.entity.Cottage;
import ru.itis.Tyshenko.repositories.*;

import java.util.Optional;

public class CottageServiceImpl implements CottageService {

    private final CottageRepository repository;

    public CottageServiceImpl(CottageRepository repository) {
        this.repository = repository;
    }

    public Optional<Cottage> get(Cottage cottage) {
        return repository.get(cottage);
    }

    public void add(Cottage cottage) {
        repository.add(cottage);
    }

    public void update(Cottage cottage) {
        repository.update(cottage);
    }

    public void delete(Cottage cottage) {
        repository.delete(cottage);
    }
}
