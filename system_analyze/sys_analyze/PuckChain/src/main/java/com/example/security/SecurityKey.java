package com.example.security;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface SecurityKey {

    KeyPair generateKeys();
    boolean isValidKeys(PublicKey publicKey, PrivateKey privateKey);
}
