package ru.itis.tyshenko.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.tyshenko.entity.Category;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findById(int id);

    @NotNull List<Category> findAll();
}
