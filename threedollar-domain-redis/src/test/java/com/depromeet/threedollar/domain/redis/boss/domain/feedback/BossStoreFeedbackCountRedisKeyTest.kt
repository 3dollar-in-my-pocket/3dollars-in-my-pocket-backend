package com.depromeet.threedollar.domain.redis.boss.domain.feedback

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val BOSS_STORE_ID = "boss-store-id"

internal class BossStoreFeedbackCountRedisKeyTest {

    @Test
    fun `가게 피드백 카운트의 키를 가져온다`() {
        // given
        val bossStoreId = "bossStoreId"
        val feedbackType = BossStoreFeedbackType.BOSS_IS_KIND
        val feedbackCountRedisKey = BossStoreFeedbackCountRedisKey(bossStoreId, feedbackType)

        // when
        val key = feedbackCountRedisKey.getKey()

        // then
        assertThat(key).isEqualTo("boss:store:feedback:bossStoreId:BOSS_IS_KIND")
    }

    @Test
    fun `가게 피드백 카운트 키의 값을 직렬화한다`() {
        // given
        val feedbackCountRedisKey = BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, BossStoreFeedbackType.BOSS_IS_KIND)

        // when
        val value = feedbackCountRedisKey.serializeValue(10)

        // then
        assertThat(value).isEqualTo("10")
    }

    @Test
    fun `가게 피드백 카운트 키의 값을 역직렬화한다`() {
        // given
        val feedbackCountRedisKey = BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, BossStoreFeedbackType.BOSS_IS_KIND)

        // when
        val value = feedbackCountRedisKey.deserializeValue("10")

        // then
        assertThat(value).isEqualTo(10)
    }

    @Test
    fun `가게의 피드백 카운트의 TTL이 없다`() {
        // given
        val feedbackCountRedisKey = BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, BossStoreFeedbackType.BOSS_IS_KIND)

        // when
        val ttl = feedbackCountRedisKey.getTtl()

        // then
        assertThat(ttl).isNull()
    }

}
