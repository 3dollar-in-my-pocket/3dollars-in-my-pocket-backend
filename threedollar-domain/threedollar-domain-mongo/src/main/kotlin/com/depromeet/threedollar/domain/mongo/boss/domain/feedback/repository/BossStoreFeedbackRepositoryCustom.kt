package com.depromeet.threedollar.domain.mongo.boss.domain.feedback.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedback
import java.time.LocalDate

interface BossStoreFeedbackRepositoryCustom {

    fun existsByStoreIdAndUserIdAndDate(storeId: String, userId: Long, date: LocalDate): Boolean

    fun findAllByBossStoreIdAndBetween(bossStoreId: String, startDate: LocalDate, endDate: LocalDate): List<BossStoreFeedback>

    fun findFirstLessThanDate(bossStoreId: String, date: LocalDate): BossStoreFeedback?

}
