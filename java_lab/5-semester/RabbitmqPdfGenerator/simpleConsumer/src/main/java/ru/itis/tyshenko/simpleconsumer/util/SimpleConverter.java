package ru.itis.tyshenko.simpleconsumer.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itis.tyshenko.simpleconsumer.dto.UserGenerateForm;

@Component
public class SimpleConverter implements Converter<String, UserGenerateForm> {

  private final Class<UserGenerateForm> formClass = UserGenerateForm.class;

  @Override
  public UserGenerateForm convert(String source) {
    String[] fieldValues = source.split(",");
    String[] fieldNames = Arrays.stream(formClass.getDeclaredFields())
        .map(Field::getName)
        .toArray(String[]::new);
    UserGenerateForm instance = new UserGenerateForm();
    for (String value : fieldValues) {
      for (String name : fieldNames) {
        if (value.contains(name)) {
          String currentValue = value.split("=")[1];
          try {
            formClass.getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1),
                String.class
            ).invoke(instance, currentValue);
          }
          catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
    return instance;
  }
}
