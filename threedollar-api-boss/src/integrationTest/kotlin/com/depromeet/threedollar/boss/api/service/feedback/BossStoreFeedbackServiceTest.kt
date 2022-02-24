package com.depromeet.threedollar.boss.api.service.feedback

import com.depromeet.threedollar.boss.api.redis.BossStoreFeedbackCounter
import com.depromeet.threedollar.boss.api.service.feedback.dto.request.AddBossStoreFeedbackRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.document.boss.document.feedback.BossStoreFeedbackCreator
import com.depromeet.threedollar.document.boss.document.feedback.BossStoreFeedbackRepository
import com.depromeet.threedollar.document.boss.document.feedback.BossStoreFeedbackType
import com.depromeet.threedollar.document.boss.document.store.BossStoreCreator
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestConstructor
import java.time.LocalDate

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class BossStoreFeedbackServiceTest(
    private val bossStoreFeedbackRepository: BossStoreFeedbackRepository,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreFeedbackService: BossStoreFeedbackService,
) {

    @MockBean
    private lateinit var bossStoreFeedbackCounter: BossStoreFeedbackCounter

    @AfterEach
    fun cleanUp() {
        bossStoreRepository.deleteAll()
        bossStoreFeedbackRepository.deleteAll()
    }

    @Test
    fun `가게에 피드백을 등록한다`() {
        // given
        val feedbackType = BossStoreFeedbackType.BOSS_IS_KIND
        val userId = 1000000L
        val date = LocalDate.of(2022, 2, 24)

        val bossStore = BossStoreCreator.create(
            bossId = "bossId",
            name = "가슴속 3천원 붕어빵 가게"
        )
        bossStoreRepository.save(bossStore)

        // when
        bossStoreFeedbackService.addFeedback(
            bossStoreId = bossStore.id,
            request = AddBossStoreFeedbackRequest(feedbackType),
            userId = userId,
            date = date
        )

        // then
        val bossStoreFeedbacks = bossStoreFeedbackRepository.findAll()
        assertAll({
            assertThat(bossStoreFeedbacks).hasSize(1)
            assertThat(bossStoreFeedbacks[0].storeId).isEqualTo(bossStore.id)
            assertThat(bossStoreFeedbacks[0].userId).isEqualTo(userId)
            assertThat(bossStoreFeedbacks[0].date).isEqualTo(date)
            assertThat(bossStoreFeedbacks[0].feedbackType).isEqualTo(feedbackType)
        })
    }

    @Test
    fun `가게 등록시 레디스에 해당 피드백에 대한 카운트가 1증가한다`() {
        // given
        val feedbackType = BossStoreFeedbackType.BOSS_IS_KIND
        val bossStore = BossStoreCreator.create(
            bossId = "bossId",
            name = "가슴속 3천원 붕어빵 가게"
        )
        bossStoreRepository.save(bossStore)

        // when
        bossStoreFeedbackService.addFeedback(
            bossStoreId = bossStore.id,
            request = AddBossStoreFeedbackRequest(feedbackType),
            userId = 1000000L,
            date = LocalDate.of(2022, 2, 24)
        )

        // then
        verify(bossStoreFeedbackCounter, times(1)).increment(bossStore.id, feedbackType)
    }

    @Test
    fun `피드백을 추가하려는 가게가 존재하지 않는 경우 NotFoundException`() {
        // when & then
        assertThatThrownBy {
            bossStoreFeedbackService.addFeedback(
                bossStoreId = "NotFound BossStore Id",
                request = AddBossStoreFeedbackRequest(BossStoreFeedbackType.BOSS_IS_KIND),
                userId = 1000L,
                date = LocalDate.of(2022, 2, 24)
            )
        }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `해당 사용자가 오늘 이미 피드백을 등록한 가게면 ConflictException이 발생한다`() {
        // given
        val feedbackType = BossStoreFeedbackType.FOOD_IS_DELICIOUS
        val userId = 1000000L
        val date = LocalDate.of(2022, 2, 24)

        val bossStore = BossStoreCreator.create(
            bossId = "bossId",
            name = "가슴속 3천원 붕어빵 가게"
        )
        bossStoreRepository.save(bossStore)

        bossStoreFeedbackRepository.save(BossStoreFeedbackCreator.create(
            storeId = bossStore.id,
            userId = userId,
            date = date,
            feedbackType = BossStoreFeedbackType.PLATING_IS_BEAUTIFUL
        ))

        // when & then
        assertThatThrownBy {
            bossStoreFeedbackService.addFeedback(
                bossStoreId = bossStore.id,
                request = AddBossStoreFeedbackRequest(feedbackType),
                userId = userId,
                date = date
            )
        }.isInstanceOf(ConflictException::class.java)
    }

    @Test
    fun `다른날 가게에 피드백을 추가했다면 피드백을 추가할 수 있다`() {
        // given
        val feedbackType = BossStoreFeedbackType.FOOD_IS_DELICIOUS
        val userId = 1000000L
        val date = LocalDate.of(2022, 2, 24)

        val bossStore = BossStoreCreator.create(
            bossId = "bossId",
            name = "가슴속 3천원 붕어빵 가게"
        )
        bossStoreRepository.save(bossStore)

        bossStoreFeedbackRepository.save(BossStoreFeedbackCreator.create(
            storeId = bossStore.id,
            userId = userId,
            date = LocalDate.of(2022, 2, 23),
            feedbackType = BossStoreFeedbackType.PLATING_IS_BEAUTIFUL
        ))

        // when
        bossStoreFeedbackService.addFeedback(
            bossStoreId = bossStore.id,
            request = AddBossStoreFeedbackRequest(feedbackType),
            userId = userId,
            date = date
        )

        // then
        val bossStoreFeedbacks = bossStoreFeedbackRepository.findAll()
        assertAll({
            assertThat(bossStoreFeedbacks).hasSize(2)
            bossStoreFeedbacks[0].let {
                assertThat(it.storeId).isEqualTo(bossStore.id)
                assertThat(it.userId).isEqualTo(userId)
                assertThat(it.date).isEqualTo(LocalDate.of(2022, 2, 23))
                assertThat(it.feedbackType).isEqualTo(BossStoreFeedbackType.PLATING_IS_BEAUTIFUL)
            }

            bossStoreFeedbacks[1].let {
                assertThat(it.storeId).isEqualTo(bossStore.id)
                assertThat(it.userId).isEqualTo(userId)
                assertThat(it.date).isEqualTo(date)
                assertThat(it.feedbackType).isEqualTo(feedbackType)
            }
        })
    }

}
