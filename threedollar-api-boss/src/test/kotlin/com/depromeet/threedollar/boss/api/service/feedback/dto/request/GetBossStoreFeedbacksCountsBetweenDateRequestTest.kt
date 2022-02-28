package com.depromeet.threedollar.boss.api.service.feedback.dto.request

import com.depromeet.threedollar.common.exception.model.InvalidException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class GetBossStoreFeedbacksCountsBetweenDateRequestTest {

    @Test
    fun `시작날짜와 종료날짜 차이가 15일을 초과하면 Invalid Exception가 발생한다`() {
        // given
        val startDate = LocalDate.of(2022, 1, 1)
        val endDate = LocalDate.of(2022, 1, 17)

        val request = GetBossStoreFeedbacksCountsBetweenDateRequest(startDate, endDate)

        // when & then
        assertThatThrownBy { request.validateRequestDateTimeInterval() }.isInstanceOf(InvalidException::class.java)
    }

    @Test
    fun `시작날짜와 종료날짜 차이가 15일 이내이면 에러가 발생하지 않는다`() {
        // given
        val startDate = LocalDate.of(2022, 1, 1)
        val endDate = LocalDate.of(2022, 1, 16)

        val request = GetBossStoreFeedbacksCountsBetweenDateRequest(startDate, endDate)

        // when & then
        assertDoesNotThrow() { request.validateRequestDateTimeInterval() }
    }

    @Test
    fun `시작날짜가 종료날짜 보다 이후인 경우 InvalidException이 발생한다`() {
        // given
        val startDate = LocalDate.of(2022, 1, 2)
        val endDate = LocalDate.of(2022, 1, 1)

        val request = GetBossStoreFeedbacksCountsBetweenDateRequest(startDate, endDate)

        // when & then
        assertThatThrownBy { request.validateRequestDateTimeInterval() }.isInstanceOf(InvalidException::class.java)
    }

    @Test
    fun `시작날짜가 종료날짜 보다 이전인 경우 에러가 발생하지 않는다`() {
        // given
        val startDate = LocalDate.of(2022, 1, 1)
        val endDate = LocalDate.of(2022, 1, 2)

        val request = GetBossStoreFeedbacksCountsBetweenDateRequest(startDate, endDate)

        // when & then
        assertDoesNotThrow() { request.validateRequestDateTimeInterval() }
    }

    @Test
    fun `시작날짜가 종료날짜와 같은 경우 에러가 발생하지 않는다`() {
        // given
        val startDate = LocalDate.of(2022, 1, 1)
        val endDate = LocalDate.of(2022, 1, 2)

        val request = GetBossStoreFeedbacksCountsBetweenDateRequest(startDate, endDate)

        // when & then
        assertDoesNotThrow() { request.validateRequestDateTimeInterval() }
    }

}
