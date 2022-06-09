package com.depromeet.threedollar.domain.mongo.domain.bossservice.store.repository.statistics

import java.time.LocalDate

interface BossDeletedStoreStatisticsRepositoryCustom {

    fun countDeletedBossStoresBetweenDate(startDate: LocalDate, endDate: LocalDate): Long

}
