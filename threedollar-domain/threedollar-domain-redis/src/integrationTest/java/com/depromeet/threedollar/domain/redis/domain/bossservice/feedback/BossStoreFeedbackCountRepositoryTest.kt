package com.depromeet.threedollar.domain.redis.domain.bossservice.feedback

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.redis.IntegrationTest
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

private const val BOSS_STORE_ID = "boss-store-id"

internal class BossStoreFeedbackCountRepositoryTest(
    private val bossStoreFeedbackCountRepository: BossStoreFeedbackCountRepository,
    private val stringRedisRepository: StringRedisRepository<BossStoreFeedbackCountKey, Int>,
) : IntegrationTest() {

    @AfterEach
    fun cleanUp() {
        for (feedbackType in BossStoreFeedbackType.values()) {
            stringRedisRepository.del(BossStoreFeedbackCountKeyFixture.create(BOSS_STORE_ID, feedbackType))
        }
    }

    @Test
    fun `가게의 모든 피드백의 총 개수를 조회한다`() {
        // given
        stringRedisRepository.set(BossStoreFeedbackCountKeyFixture.create(BOSS_STORE_ID, BossStoreFeedbackType.BOSS_IS_KIND), 1)
        stringRedisRepository.set(BossStoreFeedbackCountKeyFixture.create(BOSS_STORE_ID, BossStoreFeedbackType.GOOD_TO_EAT_IN_ONE_BITE), 2)
        stringRedisRepository.set(BossStoreFeedbackCountKeyFixture.create(BOSS_STORE_ID, BossStoreFeedbackType.FOOD_IS_DELICIOUS), 3)

        // when
        val totalCounts = bossStoreFeedbackCountRepository.getTotalCounts(BOSS_STORE_ID)

        // then
        assertThat(totalCounts).isEqualTo(6)
    }

    @Test
    fun `가게의 피드백이 하나도 없는 경우 총 개수는 0이된다`() {
        // when
        val totalCounts = bossStoreFeedbackCountRepository.getTotalCounts(BOSS_STORE_ID)

        // then
        assertThat(totalCounts).isZero
    }

    @Test
    fun `가게의 특정 피드백의 개수를 조회한다`() {
        // given
        val count = 3
        val feedbackType = BossStoreFeedbackType.BOSS_IS_KIND

        stringRedisRepository.set(BossStoreFeedbackCountKeyFixture.create(BOSS_STORE_ID, feedbackType), 3)
        stringRedisRepository.set(BossStoreFeedbackCountKeyFixture.create(BOSS_STORE_ID, BossStoreFeedbackType.GOT_A_BONUS), 2)

        // when
        val feedbackCount = bossStoreFeedbackCountRepository.getCount(BOSS_STORE_ID, feedbackType)

        // then
        assertThat(feedbackCount).isEqualTo(count)
    }

    @Test
    fun `가게의 특정 피드백의 개수를 조회한다 해당하는 피드백 카운트 키가 없을 경우 0이 반환된다`() {
        // when
        val feedbackCount = bossStoreFeedbackCountRepository.getCount(BOSS_STORE_ID, BossStoreFeedbackType.FOOD_IS_DELICIOUS)

        // then
        assertThat(feedbackCount).isZero
    }

    @Test
    fun `가게의 특정 피드백의 카운트 수를 1 증가시킨다`() {
        // given
        val feedbackType = BossStoreFeedbackType.BOSS_IS_KIND
        val key = BossStoreFeedbackCountKeyFixture.create(BOSS_STORE_ID, feedbackType)
        stringRedisRepository.set(key, 3)

        // when
        bossStoreFeedbackCountRepository.increase(BOSS_STORE_ID, feedbackType)

        // then
        val count = stringRedisRepository.get(key)
        assertThat(count).isEqualTo(4)
    }

    @Test
    fun `가게의 특정 피드백의 카운트 수를 1 증가 시킬때, 기존에 카운트 수가 없는경우 1이 된다`() {
        // given
        val feedbackType = BossStoreFeedbackType.FOOD_IS_DELICIOUS
        val key = BossStoreFeedbackCountKeyFixture.create(BOSS_STORE_ID, feedbackType)

        // when
        bossStoreFeedbackCountRepository.increase(BOSS_STORE_ID, feedbackType)

        // then
        val count = stringRedisRepository.get(key)
        assertThat(count).isEqualTo(1)
    }

    @Test
    fun `가게의 여러 피드백을 bulk로 1 증가시킨다`() {
        // given
        stringRedisRepository.set(BossStoreFeedbackCountKeyFixture.create(BOSS_STORE_ID, BossStoreFeedbackType.BOSS_IS_KIND), 3)

        // when
        bossStoreFeedbackCountRepository.increaseBulk(BOSS_STORE_ID, setOf(BossStoreFeedbackType.BOSS_IS_KIND, BossStoreFeedbackType.GOT_A_BONUS))

        // then
        val bossisKindCount = stringRedisRepository.get(BossStoreFeedbackCountKeyFixture.create(BOSS_STORE_ID, BossStoreFeedbackType.BOSS_IS_KIND))
        assertThat(bossisKindCount).isEqualTo(4)

        val platingCount = stringRedisRepository.get(BossStoreFeedbackCountKeyFixture.create(BOSS_STORE_ID, BossStoreFeedbackType.GOT_A_BONUS))
        assertThat(platingCount).isEqualTo(1)
    }

    @Test
    fun `가게의 피드백 별로 카운트 수를 조회한다`() {
        // given
        stringRedisRepository.set(BossStoreFeedbackCountKeyFixture.create(BOSS_STORE_ID, BossStoreFeedbackType.BOSS_IS_KIND), 3)
        stringRedisRepository.set(BossStoreFeedbackCountKeyFixture.create(BOSS_STORE_ID, BossStoreFeedbackType.GOT_A_BONUS), 2)
        stringRedisRepository.set(BossStoreFeedbackCountKeyFixture.create(BOSS_STORE_ID, BossStoreFeedbackType.FOOD_IS_DELICIOUS), 1)

        // when
        val counts: Map<BossStoreFeedbackType, Int> = bossStoreFeedbackCountRepository.getAllCountsGroupByFeedbackType(BOSS_STORE_ID)

        // then
        assertAll({
            assertThat(counts[BossStoreFeedbackType.BOSS_IS_KIND]).isEqualTo(3)
            assertThat(counts[BossStoreFeedbackType.GOT_A_BONUS]).isEqualTo(2)
            assertThat(counts[BossStoreFeedbackType.FOOD_IS_DELICIOUS]).isEqualTo(1)
            assertThat(counts[BossStoreFeedbackType.HANDS_ARE_FAST]).isZero
            assertThat(counts[BossStoreFeedbackType.HYGIENE_IS_CLEAN]).isZero
            assertThat(counts[BossStoreFeedbackType.CAN_PAY_BY_CARD]).isZero
            assertThat(counts[BossStoreFeedbackType.GOOD_VALUE_FOR_MONEY]).isZero
            assertThat(counts[BossStoreFeedbackType.GOOD_TO_EAT_IN_ONE_BITE]).isZero
        })
    }

}
