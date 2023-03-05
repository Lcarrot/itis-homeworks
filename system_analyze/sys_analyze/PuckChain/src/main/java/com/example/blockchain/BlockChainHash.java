package com.example.blockchain;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.ArrayUtils;
import jakarta.inject.Singleton;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Singleton
public class BlockChainHash {

    @Property(name = "signature.algorithm.digest")
    private String algorithm;
    public byte[] getHashWithPrev(String sourceData, byte[] prevHash) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorithm, new BouncyCastleProvider());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] data = sourceData.getBytes();
        data = prevHash == null ? data : ArrayUtils.concat(prevHash, data);
        return digest.digest(data);
    }

    public byte[] getHash(String sourceData) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorithm, new BouncyCastleProvider());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] data = sourceData.getBytes(StandardCharsets.UTF_8);
        return digest.digest(data);
    }

    public byte[] getHash(byte[] sourceData) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return digest.digest(sourceData);
    }
}
