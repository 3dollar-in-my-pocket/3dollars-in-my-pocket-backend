package com.depromeet.threedollar.domain.mongo.foodtruck.domain.registration.repository

import java.time.LocalDate

interface BossRegistrationStatisticsRepositoryCustom {

    fun countBossRegistrationsBetweenDate(startDate: LocalDate, endDate: LocalDate): Long

}
