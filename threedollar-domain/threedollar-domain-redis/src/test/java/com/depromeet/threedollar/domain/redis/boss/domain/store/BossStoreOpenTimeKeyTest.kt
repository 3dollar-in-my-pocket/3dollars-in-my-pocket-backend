package com.depromeet.threedollar.domain.redis.boss.domain.store

import java.time.Duration
import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import com.depromeet.threedollar.domain.redis.domain.bossservice.store.BossStoreOpenTimeKey

private const val BOSS_STORE_ID = "bossStoreId"

internal class BossStoreOpenTimeKeyTest {

    @Test
    fun `가게 오픈 정보 키의 값을 직렬화한다`() {
        // given
        val bossStoreOpenTimeKey = BossStoreOpenTimeKey(BOSS_STORE_ID)

        // when
        val value = bossStoreOpenTimeKey.serializeValue(LocalDateTime.of(2022, 1, 1, 0, 0))

        // then
        assertThat(value).isEqualToIgnoringCase("\"2022-01-01T00:00:00\"")
    }

    @Test
    fun `가게 오픈 정보 키의 값을 역직렬화한다`() {
        // given
        val bossStoreOpenTimeKey = BossStoreOpenTimeKey(BOSS_STORE_ID)

        // when
        val value = bossStoreOpenTimeKey.deserializeValue("\"2022-01-01T00:00:00\"")

        // then
        assertThat(value).isEqualTo(LocalDateTime.of(2022, 1, 1, 0, 0))
    }

    @Test
    fun `가게의 피드백 카운트의 TTL은 30분으로 설정된다`() {
        // given
        val bossStoreOpenTimeKey = BossStoreOpenTimeKey(BOSS_STORE_ID)

        // when
        val ttl = bossStoreOpenTimeKey.getTtl()

        // then
        assertThat(ttl).isEqualTo(Duration.ofMinutes(30))
    }

}

