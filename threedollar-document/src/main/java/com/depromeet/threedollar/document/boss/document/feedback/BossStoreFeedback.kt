package com.depromeet.threedollar.document.boss.document.feedback

import com.depromeet.threedollar.document.common.document.AuditingTimeDocument
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document("boss_store_feedback_v1")
class BossStoreFeedback(
    val storeId: String,
    val userId: String,
    val feedbackType: BossStoreFeedbackType
) : AuditingTimeDocument() {

    @MongoId
    lateinit var id: String

}

