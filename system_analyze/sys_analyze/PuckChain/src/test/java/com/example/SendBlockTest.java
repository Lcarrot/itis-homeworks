package com.example;

import com.example.blockchain.BlockChainSignature;
import com.example.repository.UserRepository;
import com.example.service.SendBlockToChainService;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

@MicronautTest
public class SendBlockTest {

    private static final Logger logger = LoggerFactory.getLogger(SendBlockTest.class);

    private String key = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100daaaea0f96d5d6149677f81e84af7417bdeefc254c2a9f202c8ee535afbdab2e1a30963143090e8151e2db8a2eeb119197fab6cc7efdfe73bca9600012c8f9515de994486207ffecab773af79191ae7a411d6fe33d7dc7f4a2d6cbfaefdcc67fa58908ec2d24508c11a23f388d056343cb2a85ea97f314a2b9473b56dded856c4e8250cc6937bf31a187db52066807b123fb612f76d72fed1ee91b8a2aaa756b3bf0e9640488f35941076217576a591dbede1e2176f695b18d71ad10dd4b26685d3efe17ce0baafcb8f353fe8f0e593b8cefd825d02385ba45ed4172b4232bf95bb498b1486a5c780b5e0eb22eea75b1b51ed207c95f27d7907afe3ff6df76cf0203010001";

    @Inject
    @Client("${author}")
    private HttpClient httpClient;
    @Inject
    private SendBlockToChainService service;
    @Inject
    private BlockChainSignature signature;
    @Inject
    private UserRepository userRepository;

    @Test
    void testService() {
        service.sendToService(3L);
    }

    @Test
    void sendAuthor() {
        PrivateKey privateKey = userRepository.findById(3L).get().getPrivateKey();
        String author = "Тыщенко Леонид 11-902";
        String sign = Hex.toHexString(signature.generateRsaSignature(privateKey, author.getBytes(StandardCharsets.UTF_8)));
        String post = "{\"autor\":\"" + author + "\",\"sign\":\"" + sign + "\",\"publickey\":\"" + key + "\"}";
        logger.info(httpClient.toBlocking()
                .retrieve(HttpRequest.POST("/nbc/author", post)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")));
    }
}
