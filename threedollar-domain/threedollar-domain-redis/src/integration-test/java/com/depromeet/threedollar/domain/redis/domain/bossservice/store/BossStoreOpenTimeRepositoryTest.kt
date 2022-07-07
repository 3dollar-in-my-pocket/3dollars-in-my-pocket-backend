package com.depromeet.threedollar.domain.redis.domain.bossservice.store

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.redis.IntegrationTest
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

private const val BOSS_STORE_ID = "boss-store-id"

internal class BossStoreOpenTimeRepositoryTest(
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository,
    private val stringRepository: StringRedisRepository<BossStoreOpenTimeKey, LocalDateTime>,
) : IntegrationTest() {

    @AfterEach
    fun cleanUp() {
        for (feedbackType in BossStoreFeedbackType.values()) {
            stringRepository.del(BossStoreOpenTimeKey(BOSS_STORE_ID))
        }
    }

    @Test
    fun `가게의 오픈 시간을 조회하면, 현재 영업중인 경우 영업 시작 날짜를 가져온다`() {
        // given
        val dateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
        stringRepository.set(BossStoreOpenTimeKey.of(BOSS_STORE_ID), dateTime)

        // when
        val openStartDateTime = bossStoreOpenTimeRepository.get(BOSS_STORE_ID)

        // then
        Assertions.assertThat(openStartDateTime).isEqualTo(dateTime)
    }

    @Test
    fun `가게의 오픈 시간을 가져올때, 현재 영업중이지 않은 경우 null을 반환한다`() {
        // when
        val openStartDateTime = bossStoreOpenTimeRepository.get(BOSS_STORE_ID)

        // then
        Assertions.assertThat(openStartDateTime).isNull()
    }

    @Test
    fun `가게의 영업 시작 시간을 등록한다`() {
        // given
        val dateTime = LocalDateTime.of(2022, 1, 1, 0, 0)

        // when
        bossStoreOpenTimeRepository.set(BOSS_STORE_ID, dateTime)

        // then
        val openStartDateTime = stringRepository.get(BossStoreOpenTimeKey.of(BOSS_STORE_ID))
        Assertions.assertThat(openStartDateTime).isEqualTo(dateTime)
    }

    @Test
    fun `가게의 영업 시작 시간을 삭제한다`() {
        // given
        stringRepository.set(BossStoreOpenTimeKey.of(BOSS_STORE_ID), LocalDateTime.of(2022, 1, 1, 0, 0))

        // when
        bossStoreOpenTimeRepository.delete(BOSS_STORE_ID)

        // then
        val openStartDateTime = stringRepository.get(BossStoreOpenTimeKey.of(BOSS_STORE_ID))
        Assertions.assertThat(openStartDateTime).isNull()
    }

}
