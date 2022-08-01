package com.depromeet.threedollar.domain.redis.domain.bossservice.feedback

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BossStoreFeedbackCountKeyTest {

    @Test
    fun `가게 피드백 카운트 키의 값을 직렬화한다`() {
        // given
        val feedbackCountRedisKey = BossStoreFeedbackCountKeyFixture.create()

        // when
        val value = feedbackCountRedisKey.serializeValue(10)

        // then
        assertThat(value).isEqualTo("10")
    }

    @Test
    fun `가게 피드백 카운트 키의 값을 역직렬화한다`() {
        // given
        val feedbackCountRedisKey = BossStoreFeedbackCountKeyFixture.create()

        // when
        val value = feedbackCountRedisKey.deserializeValue("10")

        // then
        assertThat(value).isEqualTo(10)
    }

    @Test
    fun `가게의 피드백 카운트의 TTL이 없다`() {
        // given
        val feedbackCountRedisKey = BossStoreFeedbackCountKeyFixture.create()

        // when
        val ttl = feedbackCountRedisKey.getTtl()

        // then
        assertThat(ttl).isNull()
    }

}
