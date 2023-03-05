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
public class AIData {

    private String w11;
    private String w12;
    private String w21;
    private String w22;
    private String v11;
    private String v12;
    private String v13;
    private String v21;
    private String v22;
    private String v23;
    private String w1;
    private String w2;
    private String w3;
    private String e;
    private String publickey;

    public String getAsString() {
        return "{" +
                "\"w11\":\"" + w11 + "\"," +
                "\"w12\":\"" + w12 + "\"," +
                "\"w21\":\"" + w21 + "\"," +
                "\"w22\":\"" + w22 + "\"," +
                "\"v11\":\"" + v11 + "\"," +
                "\"v12\":\"" + v12 + "\"," +
                "\"v13\":\"" + v13 + "\"," +
                "\"v21\":\"" + v21 + "\"," +
                "\"v22\":\"" + v22 + "\"," +
                "\"v23\":\"" + v23 + "\"," +
                "\"w1\":\"" + w1 + "\"," +
                "\"w2\":\"" + w2 + "\"," +
                "\"w3\":\"" + w3 + "\"," +
                "\"e\":\"" + e + "\"," +
                "\"publickey\":\"" + publickey + "\"}";
    }
}
