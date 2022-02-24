package com.depromeet.threedollar.document.boss.document.feedback.repository

import java.time.LocalDate

interface BossStoreFeedbackRepositoryCustom {

    fun existsByStoreIdAndUserIdAndDate(storeId: String, userId: Long, date: LocalDate): Boolean

}
