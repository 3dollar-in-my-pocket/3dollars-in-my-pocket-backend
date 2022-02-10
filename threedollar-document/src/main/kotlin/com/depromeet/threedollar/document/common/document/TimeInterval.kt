package com.depromeet.threedollar.document.common.document

import com.depromeet.threedollar.common.exception.model.InvalidException
import java.time.LocalTime

data class TimeInterval(
    val startTime: LocalTime,
    val endTime: LocalTime
) {

    init {
        if (startTime.isAfter(endTime)) {
            throw InvalidException("시작 시간 ($startTime)이 종료시간 ($endTime)보다 느릴 수 없습니다")
        }
    }

}
