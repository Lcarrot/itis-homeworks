package ru.itis.tyshenko.converter;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface Converter {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
