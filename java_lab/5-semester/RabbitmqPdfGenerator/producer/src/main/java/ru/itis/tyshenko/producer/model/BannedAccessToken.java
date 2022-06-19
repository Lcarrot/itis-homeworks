package ru.itis.tyshenko.producer.model;

import java.util.Date;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Data
@Builder
@RedisHash(value = "BannedAccessToken", timeToLive = 216000)
public class BannedAccessToken {

  @Id
  private String id;
  @Indexed
  private String accessToken;
}
