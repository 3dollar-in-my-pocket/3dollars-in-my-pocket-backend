package com.depromeet.threedollar.api.service.visit

import com.depromeet.threedollar.api.service.StoreSetupTest
import com.depromeet.threedollar.api.service.visit.dto.request.AddVisitHistoryRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.domain.visit.VisitHistory
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryCreator
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository
import com.depromeet.threedollar.domain.domain.visit.VisitType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.time.LocalDate

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class VisitHistoryServiceTest(
    private val visitHistoryService: VisitHistoryService,
    private val visitHistoryRepository: VisitHistoryRepository
) : StoreSetupTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        visitHistoryRepository.deleteAll()
    }

    @DisplayName("가게 방문 인증 등록")
    @Nested
    inner class AddStoreVisitHistory {

        @Test
        fun `유저가 가게 방문 인증을 등록하면 DB에 방문 정보가 기록된다`() {
            // given
            val request = AddVisitHistoryRequest(storeId, VisitType.EXISTS_STORE)

            // when
            visitHistoryService.addVisitHistory(request, userId)

            // then
            val histories = visitHistoryRepository.findAll()
            assertAll({
                assertThat(histories).hasSize(1)
                assertVisitHistory(
                    visitHistory = histories[0],
                    storeId = storeId,
                    userId = userId,
                    type = request.type,
                    dateOfVisit = LocalDate.now()
                )
            })
        }

        @Test
        fun `방문 인증시 존재하지 않은 가게인 경우 NotFoundException이 발생한다`() {
            // given
            val notFoundStoreId = 999L
            val request = AddVisitHistoryRequest(notFoundStoreId, VisitType.EXISTS_STORE)

            // when & then
            assertThatThrownBy {
                visitHistoryService.addVisitHistory(
                    request,
                    userId
                )
            }.isInstanceOf(NotFoundException::class.java)
        }

        @Test
        fun `이미 해당 유저가 오늘 방문한 가게인 경우 ConflictException이 발생한다`() {
            // given
            val dateOfVisit = LocalDate.of(2021, 10, 18)
            visitHistoryRepository.save(
                VisitHistoryCreator.create(
                    storeId,
                    userId,
                    VisitType.EXISTS_STORE,
                    dateOfVisit
                )
            )

            // when & then
            assertThatThrownBy {
                visitHistoryService.addVisitHistory(
                    AddVisitHistoryRequest(storeId, VisitType.NOT_EXISTS_STORE),
                    userId
                )
            }.isInstanceOf(ConflictException::class.java)
        }
    }

    private fun assertVisitHistory(
        visitHistory: VisitHistory,
        storeId: Long,
        userId: Long,
        type: VisitType,
        dateOfVisit: LocalDate
    ) {
        assertThat(visitHistory.storeId).isEqualTo(storeId)
        assertThat(visitHistory.userId).isEqualTo(userId)
        assertThat(visitHistory.type).isEqualTo(type)
        assertThat(visitHistory.dateOfVisit).isEqualTo(dateOfVisit)
    }

}
