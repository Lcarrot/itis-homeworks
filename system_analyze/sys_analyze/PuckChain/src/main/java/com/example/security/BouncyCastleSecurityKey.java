package com.example.security;

import io.micronaut.context.annotation.Property;
import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import java.util.concurrent.ThreadLocalRandom;

public class BouncyCastleSecurityKey implements SecurityKey {

    @Property(name = "signature.algorithm.sign")
    private String algorithm;

    @SneakyThrows
    public KeyPair generateKeys() {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", new BouncyCastleProvider());
        return generator.generateKeyPair();
    }

    @SneakyThrows
    public boolean isValidKeys(PublicKey publicKey, PrivateKey privateKey) {
        byte[] challenge = new byte[10000];
        ThreadLocalRandom.current().nextBytes(challenge);
        Signature sig = Signature.getInstance(algorithm, new BouncyCastleProvider());
        sig.initSign(privateKey);
        sig.update(challenge);
        byte[] signature = sig.sign();
        sig.initVerify(publicKey);
        sig.update(challenge);
        return sig.verify(signature);
    }
}
