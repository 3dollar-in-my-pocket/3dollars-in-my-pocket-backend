package com.depromeet.threedollar.domain.mongo.domain.bossservice.account.repository.statistics

import java.time.LocalDate

interface BossAccountStatisticsRepositoryCustom {

    fun countBossAccountsBetweenDate(startDate: LocalDate, endDate: LocalDate): Long

}
