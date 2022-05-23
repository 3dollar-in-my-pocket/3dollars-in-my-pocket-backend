package com.depromeet.threedollar.domain.mongo.foodtruck.domain.feedback.repository

import java.time.LocalDate

interface BossStoreFeedbackStatisticsRepositoryCustom {

    fun countBossStoreFeedbacksBetweenDate(startDate: LocalDate, endDate: LocalDate): Long

}
