package ru.itis.tyshenko.producer.model;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "RefreshToken")
public class RefreshToken {

    @Id
    private String id;
    @Indexed
    private String refreshToken;
    private Date expiredTime;
    @Indexed
    private String userEmail;
}
