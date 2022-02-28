package com.depromeet.threedollar.document.boss.document.feedback.repository

import com.depromeet.threedollar.document.boss.document.feedback.BossStoreFeedback
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.*
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

    override fun findAllByBossStoreIdAndBetween(bossStoreId: String, startDate: LocalDate, endDate: LocalDate): List<BossStoreFeedback> {
        return mongoTemplate.find(Query()
            .addCriteria(BossStoreFeedback::storeId isEqualTo bossStoreId)
            .addCriteria(BossStoreFeedback::date.gte(startDate).lte(endDate))
        )
    }

    override fun findFirstLessThanDate(date: LocalDate): BossStoreFeedback? {
        return mongoTemplate.findOne(Query()
            .addCriteria(BossStoreFeedback::date lt date).limit(1), BossStoreFeedback::class.java
        )
    }

}
