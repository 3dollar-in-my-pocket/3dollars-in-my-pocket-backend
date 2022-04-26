package com.depromeet.threedollar.domain.mongo.common.domain

import com.depromeet.threedollar.common.exception.model.InvalidException
import java.time.LocalTime

data class TimeInterval(
    val startTime: LocalTime,
    val endTime: LocalTime
) {

    init {
        if (startTime.isAfter(endTime)) {
            throw InvalidException("시작 시간($startTime)이 종료시간($endTime)보다 이후 일 수 없습니다")
        }
    }

    companion object {
        fun of(
            startTime: LocalTime,
            endTime: LocalTime
        ): TimeInterval {
            return TimeInterval(
                startTime = startTime,
                endTime = endTime
            )
        }
    }

}
