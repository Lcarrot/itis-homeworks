package com.example.entity;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class SimpleBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private LinkedBlocks linkedBlocks;
    private String data;
    private byte[] blockHashSigned;
    private byte[] prevHash;
    private byte[] prevAndBlockHashSigned;
}
