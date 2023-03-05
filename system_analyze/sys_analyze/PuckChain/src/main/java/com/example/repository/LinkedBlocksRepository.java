package com.example.repository;

import com.example.entity.LinkedBlocks;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface LinkedBlocksRepository extends JpaRepository<LinkedBlocks, Long> {

    Optional<LinkedBlocks> findByIdAndBlocksType(Long id, LinkedBlocks.BlocksType blocksType);
}
