package com.example.repository;

import com.example.entity.ArbiterBlock;
import com.example.entity.LinkedBlocks;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface ArbiterBlockRepository extends JpaRepository<ArbiterBlock, Long> {

    @Executable
    List<ArbiterBlock> findByLinkedBlocks(LinkedBlocks id);
}
