package com.example.service;

import com.example.entity.SimpleBlock;
import com.example.entity.LinkedBlocks;
import com.example.form.SourceData;
import com.example.repository.SimpleBlockRepository;
import com.example.repository.LinkedBlocksRepository;
import com.example.repository.UserRepository;
import com.example.blockchain.BlockChain;
import com.example.security.SecurityKey;
import io.micronaut.core.convert.ConversionService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.security.PublicKey;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class BlockService implements IBlockService<SourceData> {

    @Inject
    private LinkedBlocksRepository linkedBlocksRepository;
    @Inject
    private SimpleBlockRepository simpleBlockRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private SecurityKey securityKey;
    @Inject
    private BlockChain blockChain;
    @Inject
    private ConversionService<?> conversionService;

    public List<String> getDataByLinkedBlocksId(Long id, String key) {
        var publicKey = conversionService.convert(key, PublicKey.class).orElseThrow();
        LinkedBlocks linkedBlocks = linkedBlocksRepository.findByIdAndBlocksType(id, LinkedBlocks.BlocksType.SIMPLE).orElseThrow();
        List<SimpleBlock> simpleBlocks = simpleBlockRepository.findByLinkedBlocks(linkedBlocks);
        if (!blockChain.verifyBlocks(simpleBlocks, publicKey)) {
            throw new IllegalAccessError();
        }
        return simpleBlocks.stream().map(SimpleBlock::getData).collect(Collectors.toList());
    }

    @Transactional
    public Long createLinkedBlocks(SourceData sourceData) {
        var key = conversionService.convert(sourceData.getSecurity().getPublicKey(), PublicKey.class).orElseThrow();
        var user = userRepository.findById(sourceData.getSecurity().getUserId()).orElseThrow();
        var privateKey = user.getPrivateKey();
        if (!securityKey.isValidKeys(key, privateKey)) {
            throw new IllegalAccessError();
        }
        var blocks = blockChain.makeBlocks(sourceData, user.getPrivateKey());
        var linkedBlocks = LinkedBlocks.builder().user(user).blocksType(LinkedBlocks.BlocksType.SIMPLE).build();
        linkedBlocks = linkedBlocksRepository.save(linkedBlocks);
        for (SimpleBlock simpleBlock : blocks) {
            simpleBlock.setLinkedBlocks(linkedBlocks);
            simpleBlockRepository.save(simpleBlock);
        }
        return linkedBlocks.getId();
    }
}
