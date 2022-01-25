package com.depromeet.threedollar.redis.boss.domain.store;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@RedisHash(value = "boss:store:open", timeToLive = 60 * 30)
public class BossStoreOpenInfo implements Serializable {

    @Id
    private final String bossStoreId;

    private final LocalDateTime startDateTime;

    public static BossStoreOpenInfo of(String bossStoreId, LocalDateTime startDateTime) {
        return new BossStoreOpenInfo(bossStoreId, startDateTime);
    }

}
