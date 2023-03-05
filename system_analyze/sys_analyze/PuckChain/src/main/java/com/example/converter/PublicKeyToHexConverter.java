package com.example.converter;

import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import jakarta.inject.Singleton;
import org.bouncycastle.util.encoders.Hex;

import java.security.PublicKey;
import java.util.Optional;

@Singleton
public class PublicKeyToHexConverter implements TypeConverter<PublicKey, String> {

    @Override
    public Optional<String> convert(PublicKey object, Class<String> targetType, ConversionContext context) {
        return Optional.ofNullable(object).map(PublicKey::getEncoded).map(Hex::toHexString);
    }
}
