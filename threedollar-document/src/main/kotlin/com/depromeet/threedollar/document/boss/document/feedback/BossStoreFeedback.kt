package com.depromeet.threedollar.document.boss.document.feedback

import com.depromeet.threedollar.document.common.document.BaseDocument
import org.springframework.data.mongodb.core.mapping.Document

@Document("boss_store_feedback_v1")
class BossStoreFeedback(
    val storeId: String,
    val userId: String,
    val feedbackType: BossStoreFeedbackType
) : BaseDocument()

