package com.depromeet.threedollar.domain.mongo.boss.domain.account.repository

import java.time.LocalDate

interface BossAccountStatisticsRepositoryCustom {

    fun countBossAccountsBetweenDate(startDate: LocalDate, endDate: LocalDate): Long

}
