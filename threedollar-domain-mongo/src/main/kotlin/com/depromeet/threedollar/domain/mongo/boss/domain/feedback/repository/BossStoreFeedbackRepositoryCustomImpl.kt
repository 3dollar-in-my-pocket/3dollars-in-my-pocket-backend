package com.depromeet.threedollar.domain.mongo.boss.domain.feedback.repository

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedback
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.gte
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.lt
import java.time.LocalDate

class BossStoreFeedbackRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossStoreFeedbackRepositoryCustom {

    override fun existsByStoreIdAndUserIdAndFeedbackTypeAndDate(storeId: String, userId: Long, feedbackType: BossStoreFeedbackType, date: LocalDate): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(BossStoreFeedback::storeId isEqualTo storeId)
            .addCriteria(BossStoreFeedback::userId isEqualTo userId)
            .addCriteria(BossStoreFeedback::feedbackType isEqualTo feedbackType)
            .addCriteria(BossStoreFeedback::date isEqualTo date), BossStoreFeedback::class.java
        )
    }

    override fun findAllByBossStoreIdAndBetween(bossStoreId: String, startDate: LocalDate, endDate: LocalDate): List<BossStoreFeedback> {
        return mongoTemplate.find(Query()
            .addCriteria(BossStoreFeedback::storeId isEqualTo bossStoreId)
            .addCriteria(BossStoreFeedback::date.gte(startDate).lte(endDate))
        )
    }

    override fun findFirstLessThanDate(bossStoreId: String, date: LocalDate): BossStoreFeedback? {
        return mongoTemplate.findOne(Query()
            .addCriteria(BossStoreFeedback::storeId isEqualTo bossStoreId)
            .addCriteria(BossStoreFeedback::date lt date).limit(1), BossStoreFeedback::class.java
        )
    }

}
