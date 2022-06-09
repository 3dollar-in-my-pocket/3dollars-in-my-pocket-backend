package com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.repository

import java.time.LocalDate
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.gte
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.lt
import org.springframework.stereotype.Repository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.BossStoreFeedback

@Repository
class BossStoreFeedbackRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate,
) : BossStoreFeedbackRepositoryCustom {

    override fun existsByStoreIdAndUserIdAndDate(storeId: String, userId: Long, date: LocalDate): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(BossStoreFeedback::bossStoreId isEqualTo storeId)
            .addCriteria(BossStoreFeedback::userId isEqualTo userId)
            .addCriteria(BossStoreFeedback::date isEqualTo date), BossStoreFeedback::class.java
        )
    }

    override fun findAllByBossStoreIdAndBetween(bossStoreId: String, startDate: LocalDate, endDate: LocalDate): List<BossStoreFeedback> {
        return mongoTemplate.find(Query()
            .addCriteria(BossStoreFeedback::bossStoreId isEqualTo bossStoreId)
            .addCriteria(BossStoreFeedback::date.gte(startDate).lte(endDate))
        )
    }

    override fun findLastLessThanDate(bossStoreId: String, date: LocalDate): BossStoreFeedback? {
        return mongoTemplate.findOne(
            Query()
                .addCriteria(BossStoreFeedback::bossStoreId isEqualTo bossStoreId)
                .addCriteria(BossStoreFeedback::date lt date)
                .with(Sort.by(Sort.Direction.DESC, BossStoreFeedback::date.name))
                .limit(1), BossStoreFeedback::class.java
        )
    }

}
