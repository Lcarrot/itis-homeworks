package ru.itis.tyshenko.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Configuration
public class WebConfig implements WebMvcConfigurer, BeanPostProcessor {

    private final Map<Converter, ru.itis.tyshenko.converter.Converter> converterMap = new HashMap<>();

    @Autowired
    private LocalValidatorFactoryBean validatorFactoryBean;

    @Override
    public Validator getValidator() {
        return validatorFactoryBean;
    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        DefaultMessageCodesResolver codesResolver = new DefaultMessageCodesResolver();
        codesResolver.setMessageCodeFormatter(DefaultMessageCodesResolver.Format.POSTFIX_ERROR_CODE);
        return codesResolver;
    }

    @Override
    public void addFormatters(@NotNull FormatterRegistry registry) {
        converterMap.forEach((Converter converter, ru.itis.tyshenko.converter.Converter converterAnn)
                -> registry.addConverter(converter));
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        scanAnnotation(bean, Converter.class, ru.itis.tyshenko.converter.Converter.class, converterMap);
        return bean;
    }

    private <T, V extends Annotation> void scanAnnotation(Object bean, Class<T> classes, Class<V> annClass, Map<T,V> map) {
        Optional<V> optionalConverter = getAnnotation(bean.getClass(), annClass);
        if (optionalConverter.isPresent() && bean.getClass().equals(classes)) {
            map.put((T) bean, optionalConverter.get());
        }
    }

    private <T extends Annotation> Optional<T> getAnnotation(Class<?> classes, Class<T> annClass) {
        Annotation[] annotations = classes.getAnnotationsByType(annClass);
        if (annotations.length > 0) {
            return Optional.of((T) annotations[0]);
        }
        return Optional.empty();
    }
}
