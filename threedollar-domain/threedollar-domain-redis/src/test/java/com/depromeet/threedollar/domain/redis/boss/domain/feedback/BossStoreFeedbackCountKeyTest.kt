package com.depromeet.threedollar.domain.redis.boss.domain.feedback

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.redis.domain.bossservice.feedback.BossStoreFeedbackCountKey

private const val BOSS_STORE_ID = "boss-store-id"

internal class BossStoreFeedbackCountKeyTest {

    @Test
    fun `가게 피드백 카운트 키의 값을 직렬화한다`() {
        // given
        val feedbackCountRedisKey = BossStoreFeedbackCountKey(BOSS_STORE_ID, BossStoreFeedbackType.BOSS_IS_KIND)

        // when
        val value = feedbackCountRedisKey.serializeValue(10)

        // then
        assertThat(value).isEqualTo("10")
    }

    @Test
    fun `가게 피드백 카운트 키의 값을 역직렬화한다`() {
        // given
        val feedbackCountRedisKey = BossStoreFeedbackCountKey(BOSS_STORE_ID, BossStoreFeedbackType.BOSS_IS_KIND)

        // when
        val value = feedbackCountRedisKey.deserializeValue("10")

        // then
        assertThat(value).isEqualTo(10)
    }

    @Test
    fun `가게의 피드백 카운트의 TTL이 없다`() {
        // given
        val feedbackCountRedisKey = BossStoreFeedbackCountKey(BOSS_STORE_ID, BossStoreFeedbackType.BOSS_IS_KIND)

        // when
        val ttl = feedbackCountRedisKey.getTtl()

        // then
        assertThat(ttl).isNull()
    }

}
