package com.depromeet.threedollar.domain.redis.boss.domain.feedback

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

private const val BOSS_STORE_ID = "boss-store-id"

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class BossStoreFeedbackCountRedisRepositoryTest(
    private val bossStoreFeedbackCountRedisRepository: BossStoreFeedbackCountRedisRepository,
    private val stringRedisRepository: StringRedisRepository<BossStoreFeedbackCountRedisKey, Int>
) {

    @AfterEach
    fun cleanUp() {
        for (feedbackType in BossStoreFeedbackType.values()) {
            stringRedisRepository.del(BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, feedbackType))
        }
    }

    @Test
    fun `가게의 모든 피드백의 총 개수를 조회한다`() {
        // given
        stringRedisRepository.set(BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, BossStoreFeedbackType.BOSS_IS_KIND), 1)
        stringRedisRepository.set(BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, BossStoreFeedbackType.PLATING_IS_BEAUTIFUL), 2)
        stringRedisRepository.set(BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, BossStoreFeedbackType.FOOD_IS_DELICIOUS), 3)

        // when
        val totalCounts = bossStoreFeedbackCountRedisRepository.getTotalCounts(BOSS_STORE_ID)

        // then
        assertThat(totalCounts).isEqualTo(6)
    }

    @Test
    fun `가게의 특정 피드백의 개수를 조회한다`() {
        // given
        val count = 3
        val feedbackType = BossStoreFeedbackType.BOSS_IS_KIND

        stringRedisRepository.set(BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, feedbackType), 3)
        stringRedisRepository.set(BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, BossStoreFeedbackType.PLATING_IS_BEAUTIFUL), 2)

        // when
        val feedbackCount = bossStoreFeedbackCountRedisRepository.getCount(BOSS_STORE_ID, feedbackType)

        // then
        assertThat(feedbackCount).isEqualTo(count)
    }

    @Test
    fun `가게의 특정 피드백의 카운트 수를 1 증가시킨다`() {
        // given
        val feedbackType = BossStoreFeedbackType.BOSS_IS_KIND
        val key = BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, feedbackType)
        stringRedisRepository.set(key, 3)

        // when
        bossStoreFeedbackCountRedisRepository.increase(BOSS_STORE_ID, feedbackType)

        // then
        val count = stringRedisRepository.get(key)
        assertThat(count).isEqualTo(4)
    }

    @Test
    fun `가게의 특정 피드백의 카운트 수를 1 증가 시킬때, 기존에 카운트 수가 없는경우 1이 된다`() {
        // given
        val feedbackType = BossStoreFeedbackType.FOOD_IS_DELICIOUS
        val key = BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, feedbackType)

        // when
        bossStoreFeedbackCountRedisRepository.increase(BOSS_STORE_ID, feedbackType)

        // then
        val count = stringRedisRepository.get(key)
        assertThat(count).isEqualTo(1)
    }

    @Test
    fun `가게의 여러 피드백을 bulk로 1 증가시킨다`() {
        // given
        stringRedisRepository.set(BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, BossStoreFeedbackType.BOSS_IS_KIND), 3)

        // when
        bossStoreFeedbackCountRedisRepository.increaseBulk(BOSS_STORE_ID, setOf(BossStoreFeedbackType.BOSS_IS_KIND, BossStoreFeedbackType.PLATING_IS_BEAUTIFUL))

        // then
        val bossisKindCount = stringRedisRepository.get(BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, BossStoreFeedbackType.BOSS_IS_KIND))
        assertThat(bossisKindCount).isEqualTo(4)

        val platingCount = stringRedisRepository.get(BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, BossStoreFeedbackType.PLATING_IS_BEAUTIFUL))
        assertThat(platingCount).isEqualTo(1)
    }

    @Test
    fun `가게의 피드백 별로 카운트 수를 조회한다`() {
        // given
        stringRedisRepository.set(BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, BossStoreFeedbackType.BOSS_IS_KIND), 3)
        stringRedisRepository.set(BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, BossStoreFeedbackType.PLATING_IS_BEAUTIFUL), 2)
        stringRedisRepository.set(BossStoreFeedbackCountRedisKey(BOSS_STORE_ID, BossStoreFeedbackType.FOOD_IS_DELICIOUS), 1)

        // when
        val counts: Map<BossStoreFeedbackType, Int> = bossStoreFeedbackCountRedisRepository.getAllCountsGroupByFeedbackType(BOSS_STORE_ID)

        // then
        assertAll({
            assertThat(counts[BossStoreFeedbackType.BOSS_IS_KIND]).isEqualTo(3)
            assertThat(counts[BossStoreFeedbackType.PLATING_IS_BEAUTIFUL]).isEqualTo(2)
            assertThat(counts[BossStoreFeedbackType.FOOD_IS_DELICIOUS]).isEqualTo(1)
            assertThat(counts[BossStoreFeedbackType.PRICE_IS_CHEAP]).isEqualTo(0)
            assertThat(counts[BossStoreFeedbackType.THERE_ARE_PLACES_TO_EAT_AROUND]).isEqualTo(0)
            assertThat(counts[BossStoreFeedbackType.EASY_TO_EAT]).isEqualTo(0)
        })
    }

}
