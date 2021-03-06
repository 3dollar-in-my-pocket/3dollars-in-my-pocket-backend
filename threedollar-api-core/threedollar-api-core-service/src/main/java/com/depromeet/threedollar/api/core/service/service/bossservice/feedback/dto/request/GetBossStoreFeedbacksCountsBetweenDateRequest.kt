package com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.request

import com.depromeet.threedollar.common.exception.model.InvalidException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import java.time.LocalDate

private const val MAX_AVAILABLE_DAY_DIFFERENCE = 15L

data class GetBossStoreFeedbacksCountsBetweenDateRequest(
    val startDate: LocalDate,
    val endDate: LocalDate,
) {

    init {
        if (startDate.isAfter(endDate)) {
            throw InvalidException("시작 날짜($startDate)가 종료 날짜($endDate)보다 이후일 수 없습니다", ErrorCode.E400_INVALID_DATE_TIME_INTERVAL)
        }
        if (endDate.minusDays(MAX_AVAILABLE_DAY_DIFFERENCE).isAfter(startDate)) {
            throw InvalidException("시작 날짜($startDate)와 종료 날짜가 ($MAX_AVAILABLE_DAY_DIFFERENCE)일 이상 차이날 수 없습니다", ErrorCode.E400_INVALID_EXCESS_BETWEEN_START_DATE_AND_END_DATE)
        }
    }

}
