package com.depromeet.threedollar.document.common.document

import com.depromeet.threedollar.common.exception.model.ValidationException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalTime

internal class TimeIntervalTest {

    @Test
    fun 시작시간이_종료시간보다_느릴경우_VALIDATION_EXCEPTION() {
        // given
        val startTime = LocalTime.of(8, 0)
        val endTime = LocalTime.of(7, 59)

        // when & then
        Assertions.assertThatThrownBy { TimeInterval(startTime, endTime) }.isInstanceOf(ValidationException::class.java)
    }

}
