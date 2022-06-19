package ru.itis.tyshenko.producer.converter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itis.tyshenko.producer.form.Body;

@Component
public class UserGenerateConverter implements Converter<Body, String> {

  @Override
  public String convert(Body source) {
    StringBuilder builder = new StringBuilder();
    Arrays.stream(source.getClass().getDeclaredFields()).map(Field::getName).forEach(field -> {
      try {
        builder.append(field)
            .append("=")
            .append(source.getClass()
                .getMethod("get" + field.substring(0, 1).toUpperCase() + field.substring(1))
                .invoke(source))
        .append(",");
      }
      catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    });
    return builder.deleteCharAt(builder.length() - 1).toString();
  }
}
