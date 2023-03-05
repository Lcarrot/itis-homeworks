package com.example.form;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class SourceDataWithArbiter {

    @Builder.Default
    private List<String> data = new ArrayList<>();
    private Long userId;
}
