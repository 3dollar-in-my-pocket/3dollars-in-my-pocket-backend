package com.depromeet.threedollar.domain.mongo.common.domain

import com.depromeet.threedollar.common.exception.model.InvalidException
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import java.time.LocalTime

internal class TimeIntervalTest : FunSpec({

    test("시작 시간이 종료 시간보다 이전인 경우, 에러가 발생하지 않는다") {
        // given
        val startTime = LocalTime.of(8, 0)
        val endTime = LocalTime.of(8, 1)

        // when & then
        shouldNotThrowAny {
            TimeInterval(startTime, endTime)
        }
    }

    test("시작 시간이, 종료시간 보다 이후인경우 Invalid Exception") {
        // given
        val startTime = LocalTime.of(8, 0)
        val endTime = LocalTime.of(7, 59)

        // when & then
        shouldThrowExactly<InvalidException> {
            TimeInterval(startTime, endTime)
        }
    }

})
