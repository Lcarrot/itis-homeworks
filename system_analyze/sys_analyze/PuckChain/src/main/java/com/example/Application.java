package com.example;

import io.micronaut.runtime.Micronaut;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Application {
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        Micronaut.run(Application.class, args);
    }
}
