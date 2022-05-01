package com.depromeet.threedollar.domain.mongo.boss.domain.store.repository

import java.time.LocalDate

interface BossStoreLocationStatisticsRepositoryCustom {

    fun countUpdatedBossStoreLocationsBetweenDate(startDate: LocalDate, endDate: LocalDate): Long

}
