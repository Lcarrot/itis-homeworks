package com.example;

import com.example.entity.SimpleBlock;
import com.example.form.SourceData;
import com.example.generator.Generator;
import com.example.blockchain.BlockChain;
import com.example.security.BouncyCastleSecurityKey;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;

import java.security.KeyPair;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@MicronautTest
class  BlocksTest {

    @Inject
    BlockChain blockChain;
    @Inject
    BouncyCastleSecurityKey keyGenerator;

    private List<SimpleBlock> simpleBlocks;
    private KeyPair keyPair;

    private int index;

    @BeforeEach
    void createData() {
        SourceData sourceData = Generator.generateSourceData();
        keyPair = keyGenerator.generateKeys();
        simpleBlocks = blockChain.makeBlocks(sourceData, keyPair.getPrivate());
        index = ThreadLocalRandom.current().nextInt(simpleBlocks.size());
    }

    @Test
    void testNormalBehavior() {
        Assertions.assertTrue(blockChain.verifyBlocks(simpleBlocks, keyPair.getPublic()));
    }

    @Test
    void testRemoveBlockBehavior() {
        simpleBlocks.remove(index);
        Assertions.assertFalse(blockChain.verifyBlocks(simpleBlocks, keyPair.getPublic()));
    }

    @Test
    void testChangeDateBehavior() {
        SimpleBlock simpleBlock = simpleBlocks.get(index);
        simpleBlock.setData("Hello, world");
        Assertions.assertFalse(blockChain.verifyBlocks(simpleBlocks, keyPair.getPublic()));
    }
}
