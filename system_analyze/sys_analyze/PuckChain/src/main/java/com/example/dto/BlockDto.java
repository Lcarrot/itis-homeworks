package com.example.dto;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class BlockDto {

    private String prevhash;
    private AIData data;
    private String signature;
    private String info;
    private String ts;
    private String arbitersignature;

    public String getAsString() {
        return "{" +
                "\"prevhash\":\"" + prevhash + "\"," +
                "\"data\":" + data.toString() + "," +
                "\"signature\":\"" + signature + "\"," +
                "\"info\":\"" + info + "\"," +
                "\"ts\":\"" + ts + "\"," +
                "\"arbitersignature\":\"" + arbitersignature + "\"," +
                "\"info\":\"" + info + "\"}";
    }
}
