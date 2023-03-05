package com.example.service;

import com.example.blockchain.BlockChainHash;
import com.example.blockchain.BlockChainSignature;
import com.example.dto.TimeStampResp;
import com.example.entity.ArbiterBlock;
import com.example.entity.LinkedBlocks;
import com.example.form.SourceDataWithArbiter;
import com.example.repository.ArbiterBlockRepository;
import com.example.repository.LinkedBlocksRepository;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.convert.ConversionService;
import io.micronaut.core.util.ArrayUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import org.bouncycastle.util.encoders.Hex;

import javax.transaction.Transactional;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class BlockWithArbiterService implements IBlockService<SourceDataWithArbiter> {

    @Inject
    private UserRepository userRepository;
    @Inject
    private LinkedBlocksRepository linkedBlocksRepository;
    @Inject
    private ArbiterBlockRepository arbiterBlockRepository;
    @Inject
    private ConversionService<?> conversionService;
    @Inject
    private ObjectMapper objectMapper;
    @Inject
    private BlockChainSignature blockChainSignature;
    @Inject
    private BlockChainHash blockChainHash;
    @Inject
    @Client("${arbiter.url}")
    private HttpClient httpClient;
    @Property(name = "arbiter.key")
    private String keyPath;

    @Transactional
    public Long createLinkedBlocks(SourceDataWithArbiter sourceData) {
        var user = userRepository.findById(sourceData.getUserId()).orElseThrow();
        var blocks = createArbiterBlocks(sourceData);
        var linkedBlocks = LinkedBlocks.builder().user(user).blocksType(LinkedBlocks.BlocksType.ARBITER).build();
        linkedBlocks = linkedBlocksRepository.save(linkedBlocks);
        for (ArbiterBlock block : blocks) {
            block.setLinkedBlocks(linkedBlocks);
            arbiterBlockRepository.save(block);
        }
        return linkedBlocks.getId();
    }

    public List<String> getDataByLinkedBlocksId(Long id) {
        var publicKey = getPublicKeyByApi();
        LinkedBlocks linkedBlocks = linkedBlocksRepository.findByIdAndBlocksType(id, LinkedBlocks.BlocksType.ARBITER).orElseThrow();
        List<ArbiterBlock> simpleBlocks = arbiterBlockRepository.findByLinkedBlocks(linkedBlocks);
        if (!verifyBlocks(simpleBlocks, publicKey)) {
            throw new IllegalAccessError();
        }
        return simpleBlocks.stream().map(ArbiterBlock::getData).collect(Collectors.toList());
    }

    private PublicKey getPublicKeyByApi() {
        String response = httpClient.toBlocking().retrieve(HttpRequest.GET(keyPath));
        return conversionService.convert(response, PublicKey.class).orElseThrow();
    }

    @SneakyThrows
    private List<ArbiterBlock> createArbiterBlocks(SourceDataWithArbiter sourceData) {
        byte[] prevHash = null;
        List<ArbiterBlock> simpleBlocks = new ArrayList<>();
        for (String data : sourceData.getData()) {
            var hash = blockChainHash.getHash(data);
            var withPrevHash = blockChainHash.getHashWithPrev(data, prevHash);

            var block = new ArbiterBlock();
            block.setData(data);
            String response = httpClient.toBlocking().retrieve(HttpRequest.GET("?digest=" + Hex.toHexString(withPrevHash)));
            var timeStampResp = objectMapper.readValue(response, TimeStampResp.class);
            if (timeStampResp.getStatus() != 0) {
                throw new IllegalArgumentException(timeStampResp.getStatusString());
            }
            block.setPrevAndBlockHashSigned(Hex.decode(timeStampResp.getTimeStampToken().getSignature()));
            block.setPrevHash(prevHash);
            block.setDate(timeStampResp.getTimeStampToken().getTs());
            simpleBlocks.add(block);
            prevHash = hash;
        }
        return simpleBlocks;
    }

    public boolean verifyBlocks(List<ArbiterBlock> simpleBlocks, PublicKey publicKey) {
        if (simpleBlocks.isEmpty()) {
            return false;
        }
        var blockIterator = simpleBlocks.iterator();
        var firstSimpleBlock = blockIterator.next();
        byte[] prevHash = blockChainHash.getHash(firstSimpleBlock.getData());
        while (blockIterator.hasNext()) {
            var block = blockIterator.next();
            if (!Arrays.equals(prevHash, block.getPrevHash())) {
                return false;
            }
            var withPrevHash = blockChainHash.getHashWithPrev(block.getData(), block.getPrevHash());
            var withTimeStamp = ArrayUtils.concat(block.getDate().getBytes(), withPrevHash);
            if (!blockChainSignature.verifyRsaSignature(publicKey, withTimeStamp, block.getPrevAndBlockHashSigned())) {
                return false;
            }
            prevHash = blockChainHash.getHash(block.getData());
        }
        return true;
    }
}
