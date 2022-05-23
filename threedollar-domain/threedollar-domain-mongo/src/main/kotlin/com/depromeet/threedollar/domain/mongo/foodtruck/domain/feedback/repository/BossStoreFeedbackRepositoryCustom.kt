package com.depromeet.threedollar.domain.mongo.foodtruck.domain.feedback.repository

import java.time.LocalDate
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.feedback.BossStoreFeedback

interface BossStoreFeedbackRepositoryCustom {

    fun existsByStoreIdAndUserIdAndDate(storeId: String, userId: Long, date: LocalDate): Boolean

    fun findAllByBossStoreIdAndBetween(bossStoreId: String, startDate: LocalDate, endDate: LocalDate): List<BossStoreFeedback>

    fun findFirstLessThanDate(bossStoreId: String, date: LocalDate): BossStoreFeedback?

}
