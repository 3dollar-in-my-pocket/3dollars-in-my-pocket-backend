package com.depromeet.threedollar.domain.mongo.boss.domain.store.repository

import java.time.LocalDate

interface BossStoreStatisticsRepositoryCustom {

    fun countBossStoresBetweenDate(startDate: LocalDate, endDate: LocalDate): Long

}
