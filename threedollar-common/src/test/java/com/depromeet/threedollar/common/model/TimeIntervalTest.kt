package com.depromeet.threedollar.common.model

import java.time.LocalTime
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import com.depromeet.threedollar.common.exception.model.InvalidException

internal class TimeIntervalTest {

    @Test
    fun `시작시간이 종료시간 이전인경우 에러가 발생하지 않는다`() {
        // given
        val startTime = LocalTime.of(8, 0)
        val endTime = LocalTime.of(8, 1)

        // when & then
        assertDoesNotThrow { TimeInterval(startTime, endTime) }
    }

    @Test
    fun `시작시간이 종료시간과 동일한경우 에러가 발생하지 않는다`() {
        // given
        val dateTime = LocalTime.of(8, 0)

        // when & then
        assertDoesNotThrow { TimeInterval(dateTime, dateTime) }
    }

    @Test
    fun 시작시간이_종료시간보다_느릴경우_INVALID_EXCEPTION() {
        // given
        val startTime = LocalTime.of(8, 0)
        val endTime = LocalTime.of(7, 59)

        // when & then
        assertThatThrownBy { TimeInterval(startTime, endTime) }.isInstanceOf(InvalidException::class.java)
    }

}
