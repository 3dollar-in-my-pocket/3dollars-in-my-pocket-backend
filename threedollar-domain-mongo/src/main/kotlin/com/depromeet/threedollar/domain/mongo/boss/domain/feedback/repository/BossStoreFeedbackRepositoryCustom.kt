package com.depromeet.threedollar.domain.mongo.boss.domain.feedback.repository

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedback
import java.time.LocalDate

interface BossStoreFeedbackRepositoryCustom {

    fun existsByStoreIdAndUserIdAndFeedbackTypeAndDate(storeId: String, userId: Long, feedbackType: BossStoreFeedbackType, date: LocalDate): Boolean

    fun findAllByBossStoreIdAndBetween(bossStoreId: String, startDate: LocalDate, endDate: LocalDate): List<BossStoreFeedback>

    fun findFirstLessThanDate(bossStoreId: String, date: LocalDate): BossStoreFeedback?

}
