package com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.repository.statistics

import java.time.LocalDate

interface BossStoreFeedbackStatisticsRepositoryCustom {

    fun countBossStoreFeedbacksBetweenDate(startDate: LocalDate, endDate: LocalDate): Long

}
