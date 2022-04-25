package com.depromeet.threedollar.domain.mongo.boss.domain.feedback.repository

import java.time.LocalDate

interface BossStoreFeedbackStatisticsRepositoryCustom {

    fun countAllBossStoreFeedbacks(): Long

    fun countBossStoreFeedbacksBetweenDate(startDate: LocalDate, endDate: LocalDate): Long

}
