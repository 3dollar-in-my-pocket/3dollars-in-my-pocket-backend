package com.depromeet.threedollar.domain.mongo.foodtruck.domain.store.repository

import java.time.LocalDate

interface BossStoreStatisticsRepositoryCustom {

    fun countBossStoresBetweenDate(startDate: LocalDate, endDate: LocalDate): Long

}
