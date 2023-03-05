package com.example.repository;

import com.example.entity.SimpleBlock;
import com.example.entity.LinkedBlocks;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface SimpleBlockRepository extends JpaRepository<SimpleBlock, Long> {

    @Executable
    List<SimpleBlock> findByLinkedBlocks(LinkedBlocks id);
}
