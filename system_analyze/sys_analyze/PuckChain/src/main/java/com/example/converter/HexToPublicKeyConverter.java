package com.example.converter;

import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import jakarta.inject.Singleton;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;

@Singleton
public class HexToPublicKeyConverter implements TypeConverter<String, PublicKey> {

    @Override
    public Optional<PublicKey> convert(String object, Class<PublicKey> targetType, ConversionContext context) {
        return Optional.ofNullable(object).map(Hex::decode).map(encoded -> {
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encoded);
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
                return keyFactory.generatePublic(pubKeySpec);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
