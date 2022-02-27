package com.depromeet.threedollar.boss.api.service.feedback.dto.request

import com.depromeet.threedollar.common.exception.model.InvalidException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import java.time.LocalDate

data class GetBossStoreFeedbacksCountsBetweenDateRequest(
    val startDate: LocalDate,
    val endDate: LocalDate
) {

    fun validateRequestDateTimeInterval() {
        if (startDate.isAfter(endDate)) {
            throw InvalidException("시작 날짜($startDate)가 종료 날짜($endDate)보다 이후일 수 없습니다", ErrorCode.INVALID_DATE_TIME_INTERVAL)
        }
    }

}
