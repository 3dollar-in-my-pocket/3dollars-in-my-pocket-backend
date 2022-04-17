package com.depromeet.threedollar.api.core.service.feedback

import com.depromeet.threedollar.api.core.service.SetupBossStoreServiceTest
import com.depromeet.threedollar.api.core.service.boss.feedback.BossStoreFeedbackService
import com.depromeet.threedollar.api.core.service.boss.feedback.dto.request.AddBossStoreFeedbackRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedbackCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedbackRepository
import com.depromeet.threedollar.domain.redis.boss.domain.feedback.BossStoreFeedbackCountRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.time.LocalDate

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class BossStoreFeedbackServiceTest(
    private val bossStoreFeedbackRepository: BossStoreFeedbackRepository,
    private val bossStoreFeedbackService: BossStoreFeedbackService,
    private val bossStoreFeedbackCountRepository: BossStoreFeedbackCountRepository
) : SetupBossStoreServiceTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossStoreRepository.deleteAll()
        bossStoreFeedbackRepository.deleteAll()
    }

    @Test
    fun `특정 가게에 피드백을 추가한다`() {
        // given
        val feedbackType1 = BossStoreFeedbackType.BOSS_IS_KIND
        val feedbackType2 = BossStoreFeedbackType.FOOD_IS_DELICIOUS

        val date = LocalDate.of(2022, 2, 24)

        // when
        bossStoreFeedbackService.addFeedback(
            bossStoreId = bossStoreId,
            request = AddBossStoreFeedbackRequest(setOf(feedbackType1, feedbackType2)),
            userId = userId,
            date = date
        )

        // then
        val bossStoreFeedbacks = bossStoreFeedbackRepository.findAll()
        assertAll({
            assertThat(bossStoreFeedbacks).hasSize(2)
            assertThat(bossStoreFeedbacks[0].bossStoreId).isEqualTo(bossStoreId)
            assertThat(bossStoreFeedbacks[0].userId).isEqualTo(userId)
            assertThat(bossStoreFeedbacks[0].date).isEqualTo(date)
            assertThat(bossStoreFeedbacks[0].feedbackType).isEqualTo(feedbackType1)

            assertThat(bossStoreFeedbacks[1].bossStoreId).isEqualTo(bossStoreId)
            assertThat(bossStoreFeedbacks[1].userId).isEqualTo(userId)
            assertThat(bossStoreFeedbacks[1].date).isEqualTo(date)
            assertThat(bossStoreFeedbacks[1].feedbackType).isEqualTo(feedbackType2)
        })
    }

    @Test
    fun `피드백을 등록하면 레디스에 해당 피드백 종류의 전체 피드백 갯수가 1증가한다`() {
        // given
        val feedbackType = BossStoreFeedbackType.BOSS_IS_KIND

        // when
        bossStoreFeedbackService.addFeedback(
            bossStoreId = bossStoreId,
            request = AddBossStoreFeedbackRequest(setOf(feedbackType)),
            userId = userId,
            date = LocalDate.of(2022, 2, 24)
        )

        // then
        val count = bossStoreFeedbackCountRepository.getCount(bossStoreId = bossStoreId, feedbackType = feedbackType)
        assertThat(count).isEqualTo(1)
    }

    @Test
    fun `피드백을 추가하려는 가게가 존재하지 않는 경우 NotFoundException`() {
        // when & then
        assertThatThrownBy {
            bossStoreFeedbackService.addFeedback(
                bossStoreId = "NotFound BossStore Id",
                request = AddBossStoreFeedbackRequest(setOf(BossStoreFeedbackType.BOSS_IS_KIND)),
                userId = userId,
                date = LocalDate.of(2022, 2, 24)
            )
        }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `해당 사용자가 오늘 이미 같은 피드백을 등록한 가게면 ConflictException이 발생한다`() {
        // given
        val feedbackType = BossStoreFeedbackType.FOOD_IS_DELICIOUS
        val date = LocalDate.of(2022, 2, 24)

        bossStoreFeedbackRepository.save(BossStoreFeedbackCreator.create(
            storeId = bossStoreId,
            userId = userId,
            date = date,
            feedbackType = feedbackType
        ))

        // when & then
        assertThatThrownBy {
            bossStoreFeedbackService.addFeedback(
                bossStoreId = bossStoreId,
                request = AddBossStoreFeedbackRequest(setOf(feedbackType)),
                userId = userId,
                date = date
            )
        }.isInstanceOf(ConflictException::class.java)
    }

    @Test
    fun `해당 사용자가 오늘 다른 같은 피드백을 등록한 가게인 경우 ConflictException이 발생한다`() {
        // given
        val feedbackType = BossStoreFeedbackType.FOOD_IS_DELICIOUS
        val date = LocalDate.of(2022, 2, 24)

        bossStoreFeedbackRepository.save(BossStoreFeedbackCreator.create(
            storeId = bossStoreId,
            userId = userId,
            date = date,
            feedbackType = BossStoreFeedbackType.BOSS_IS_KIND
        ))

        // when & then
        assertThatThrownBy {
            bossStoreFeedbackService.addFeedback(
                bossStoreId = bossStoreId,
                request = AddBossStoreFeedbackRequest(setOf(feedbackType)),
                userId = userId,
                date = date
            )
        }.isInstanceOf(ConflictException::class.java)
    }

    @Test
    fun `다른날 가게에 피드백을 추가했다면 피드백을 추가할 수 있다`() {
        // given
        val feedbackType = BossStoreFeedbackType.FOOD_IS_DELICIOUS
        val date = LocalDate.of(2022, 2, 24)

        bossStoreFeedbackRepository.save(BossStoreFeedbackCreator.create(
            storeId = bossStoreId,
            userId = userId,
            date = LocalDate.of(2022, 2, 23),
            feedbackType = BossStoreFeedbackType.PLATING_IS_BEAUTIFUL
        ))

        // when
        bossStoreFeedbackService.addFeedback(
            bossStoreId = bossStoreId,
            request = AddBossStoreFeedbackRequest(setOf(feedbackType)),
            userId = userId,
            date = date
        )

        // then
        val bossStoreFeedbacks = bossStoreFeedbackRepository.findAll()
        assertAll({
            assertThat(bossStoreFeedbacks).hasSize(2)
            bossStoreFeedbacks[0].let {
                assertThat(it.bossStoreId).isEqualTo(bossStoreId)
                assertThat(it.userId).isEqualTo(userId)
                assertThat(it.date).isEqualTo(LocalDate.of(2022, 2, 23))
                assertThat(it.feedbackType).isEqualTo(BossStoreFeedbackType.PLATING_IS_BEAUTIFUL)
            }

            bossStoreFeedbacks[1].let {
                assertThat(it.bossStoreId).isEqualTo(bossStoreId)
                assertThat(it.userId).isEqualTo(userId)
                assertThat(it.date).isEqualTo(LocalDate.of(2022, 2, 24))
                assertThat(it.feedbackType).isEqualTo(feedbackType)
            }
        })
    }

}
