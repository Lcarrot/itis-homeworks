package ru.itis.tyshenko.converter;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Priority;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import ru.itis.tyshenko.entity.Category;
import ru.itis.tyshenko.service.CategoryService;

@Log4j
@Converter
public class CustomConverter implements org.springframework.core.convert.converter.Converter<String, Category> {

    @Autowired
    private CategoryService categoryService;

    @Override
    public Category convert(@NotNull String s) {
        log.log(Priority.ERROR, "gg");
        return categoryService.findById(Integer.parseInt(s));
    }
}
