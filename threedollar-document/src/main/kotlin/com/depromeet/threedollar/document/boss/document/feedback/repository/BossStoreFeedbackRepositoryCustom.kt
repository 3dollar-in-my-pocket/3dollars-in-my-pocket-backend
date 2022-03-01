package com.depromeet.threedollar.document.boss.document.feedback.repository

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.document.boss.document.feedback.BossStoreFeedback
import java.time.LocalDate

interface BossStoreFeedbackRepositoryCustom {

    fun existsByStoreIdAndUserIdAndFeedbackTypeAndDate(storeId: String, userId: Long, feedbackType: BossStoreFeedbackType, date: LocalDate): Boolean

    fun findAllByBossStoreIdAndBetween(bossStoreId: String, startDate: LocalDate, endDate: LocalDate): List<BossStoreFeedback>

    fun findFirstLessThanDate(date: LocalDate): BossStoreFeedback?

}
