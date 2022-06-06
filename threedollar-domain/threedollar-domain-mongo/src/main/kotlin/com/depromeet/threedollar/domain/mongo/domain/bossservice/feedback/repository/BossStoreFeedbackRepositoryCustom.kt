package com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.repository

import java.time.LocalDate
import com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.BossStoreFeedback

interface BossStoreFeedbackRepositoryCustom {

    fun existsByStoreIdAndUserIdAndDate(storeId: String, userId: Long, date: LocalDate): Boolean

    fun findAllByBossStoreIdAndBetween(bossStoreId: String, startDate: LocalDate, endDate: LocalDate): List<BossStoreFeedback>

    fun findLastLessThanDate(bossStoreId: String, date: LocalDate): BossStoreFeedback?

}