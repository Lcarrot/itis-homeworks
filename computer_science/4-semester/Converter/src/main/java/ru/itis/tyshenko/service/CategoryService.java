package ru.itis.tyshenko.service;

import ru.itis.tyshenko.entity.Category;
import java.util.List;

public interface CategoryService {

    Category findById(int id);
    List<Category> findAllCategories();
}
