package com.depromeet.threedollar.domain.mongo.boss.domain.feedback

import java.time.LocalDate
import org.springframework.data.mongodb.core.mapping.Document
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.common.domain.BaseDocument

@Document("boss_store_feedback_v1")
class BossStoreFeedback(
    val bossStoreId: String,
    val userId: Long,
    val feedbackType: BossStoreFeedbackType,
    val date: LocalDate
) : BaseDocument() {

    companion object {
        fun of(
            bossStoreId: String,
            userId: Long,
            feedbackType: BossStoreFeedbackType,
            date: LocalDate
        ): BossStoreFeedback {
            return BossStoreFeedback(
                bossStoreId = bossStoreId,
                userId = userId,
                feedbackType = feedbackType,
                date = date
            )
        }
    }

}
