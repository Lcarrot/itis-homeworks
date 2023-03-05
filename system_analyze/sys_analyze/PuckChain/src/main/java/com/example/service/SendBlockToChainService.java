package com.example.service;

import com.example.blockchain.BlockChainHash;
import com.example.blockchain.BlockChainSignature;
import com.example.dto.AIData;
import com.example.dto.BlockDto;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.type.Argument;
import io.micronaut.core.util.ArrayUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.List;

@Singleton
public class SendBlockToChainService {

    private static final Logger logger = LoggerFactory.getLogger(SendBlockToChainService.class);

    @Inject
    @Client("${block.url}")
    private HttpClient httpClient;
    @Inject
    private BlockChainHash blockChainHash;
    @Inject
    private ResourceLoader loader;
    @Inject
    private BlockChainSignature blockChainSignature;
    @Inject
    private UserRepository userRepository;
    @Inject
    private ObjectMapper objectMapper;

    @Property(name = "block.all")
    private String allBlocks;
    @Property(name = "block.new")
    private String newBlockPath;

    @SneakyThrows
    public void sendToService(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        PrivateKey privateKey = user.getPrivateKey();
        BlockDto newBlock = new BlockDto();
        newBlock.setPrevhash(getPrevHash());
        logger.info(newBlock.getPrevhash());
        AIData data = objectMapper.readValue(new String(loader
                .getResourceAsStream("answer3.json").get().readAllBytes()), AIData.class);
        newBlock.setData(data);
        newBlock.setSignature(Hex.toHexString(blockChainSignature.generateRsaSignature(privateKey,
                data.getAsString().getBytes())));
        logger.info(httpClient.toBlocking()
                .retrieve(HttpRequest.POST(newBlockPath, newBlock)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")));
    }

    public String getPrevHash() {
        List<BlockDto> response = (List<BlockDto>) httpClient.toBlocking()
                .retrieve(HttpRequest.GET(allBlocks), Argument.of(List.class, BlockDto.class));
        BlockDto last = response.get(response.size() - 1);
        return Hex.toHexString(blockChainHash.getHash(
                ArrayUtils.concat(
                        ArrayUtils.concat(Hex.decode(last.getPrevhash()),
                                last.getData().getAsString().getBytes(StandardCharsets.UTF_8)),
                        Hex.decode(last.getSignature()))));
    }
}
