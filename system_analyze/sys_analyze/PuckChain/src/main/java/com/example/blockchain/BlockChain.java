package com.example.blockchain;

import com.example.entity.SimpleBlock;
import com.example.form.SourceData;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Singleton
public class BlockChain {

    @Inject
    private BlockChainSignature blockChainSignature;
    @Inject
    private BlockChainHash blockChainHash;

    public List<SimpleBlock> makeBlocks(SourceData sourceData, PrivateKey privateKey) {
        byte[] prevHash = null;
        List<SimpleBlock> simpleBlocks = new ArrayList<>();
        for (String data : sourceData.getData()) {
            var hash = blockChainHash.getHash(data);
            var hashSigned = blockChainSignature.generateRsaSignature(privateKey, hash);
            var withPrevSigned = blockChainSignature.generateRsaSignature(privateKey, blockChainHash.getHashWithPrev(data, prevHash));
            var block = new SimpleBlock();
            block.setData(data);
            block.setBlockHashSigned(hashSigned);
            block.setPrevHash(prevHash);
            block.setPrevAndBlockHashSigned(withPrevSigned);
            simpleBlocks.add(block);
            prevHash = hash;
        }
        return simpleBlocks;
    }

    public boolean verifyBlocks(List<SimpleBlock> simpleBlocks, PublicKey publicKey) {
        if (simpleBlocks.isEmpty()) {
            return false;
        }
        Iterator<SimpleBlock> blockIterator = simpleBlocks.iterator();
        SimpleBlock firstSimpleBlock = blockIterator.next();
        byte[] prevHash = blockChainHash.getHash(firstSimpleBlock.getData());
        while (blockIterator.hasNext()) {
            SimpleBlock simpleBlock = blockIterator.next();
            if (!Arrays.equals(prevHash, simpleBlock.getPrevHash())) {
                return false;
            }
            var withPrevHash = blockChainHash.getHashWithPrev(simpleBlock.getData(), simpleBlock.getPrevHash());
            if (!blockChainSignature.verifyRsaSignature(publicKey, withPrevHash, simpleBlock.getPrevAndBlockHashSigned())) {
                return false;
            }
            prevHash = blockChainHash.getHash(simpleBlock.getData());
            if (!blockChainSignature.verifyRsaSignature(publicKey, prevHash, simpleBlock.getBlockHashSigned())) {
                return false;
            }
        }
        return true;
    }
}
