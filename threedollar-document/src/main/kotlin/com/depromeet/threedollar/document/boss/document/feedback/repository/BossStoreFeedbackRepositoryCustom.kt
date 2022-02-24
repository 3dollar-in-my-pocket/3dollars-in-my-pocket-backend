package com.depromeet.threedollar.document.boss.document.feedback.repository

import com.depromeet.threedollar.document.boss.document.feedback.BossStoreFeedback
import java.time.LocalDate

interface BossStoreFeedbackRepositoryCustom {

    fun existsByStoreIdAndUserIdAndDate(storeId: String, userId: Long, date: LocalDate): Boolean

    fun findAllByBossStoreIdAndBetween(bossStoreId: String, startDate: LocalDate, endDate: LocalDate): List<BossStoreFeedback>

}
