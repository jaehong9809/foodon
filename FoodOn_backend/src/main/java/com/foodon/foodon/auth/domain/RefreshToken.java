package com.foodon.foodon.auth.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "jwt", timeToLive = 60 * 60 * 24 * 14) // TTL 2주
public class RefreshToken {

    private Long userId;

    @Id
    private String value;
}
