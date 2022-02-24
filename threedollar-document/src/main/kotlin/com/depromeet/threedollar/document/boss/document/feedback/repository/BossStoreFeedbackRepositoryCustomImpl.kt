package com.depromeet.threedollar.document.boss.document.feedback.repository

import com.depromeet.threedollar.document.boss.document.feedback.BossStoreFeedback
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import java.time.LocalDate

class BossStoreFeedbackRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossStoreFeedbackRepositoryCustom {

    override fun existsByStoreIdAndUserIdAndDate(storeId: String, userId: Long, date: LocalDate): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(BossStoreFeedback::storeId isEqualTo storeId)
            .addCriteria(BossStoreFeedback::userId isEqualTo userId)
            .addCriteria(BossStoreFeedback::date isEqualTo date), BossStoreFeedback::class.java
        )
    }

}
