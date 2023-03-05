package com.example.blockchain;

import io.micronaut.context.annotation.Property;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

@Singleton
public class BlockChainSignature {

    @Property(name = "signature.algorithm.sign")
    private String algorithm;

    @SneakyThrows
    public byte[] generateRsaSignature(PrivateKey privateKey, byte[] input) {
        Signature signature = Signature.getInstance(algorithm, new BouncyCastleProvider());
        signature.initSign(privateKey);
        signature.update(input);
        return signature.sign();
    }

    @SneakyThrows
    public boolean verifyRsaSignature(PublicKey publicKey, byte[] input, byte[] encSignature) {
        Signature signature = Signature.getInstance(algorithm, new BouncyCastleProvider());
        signature.initVerify(publicKey);
        signature.update(input);
        return signature.verify(encSignature);
    }
}