package com.depromeet.threedollar.domain.mongo.boss.domain.feedback

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.common.domain.BaseDocument
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document("boss_store_feedback_v1")
class BossStoreFeedback(
    val storeId: String,
    val userId: Long,
    val feedbackType: BossStoreFeedbackType,
    val date: LocalDate
) : BaseDocument()

