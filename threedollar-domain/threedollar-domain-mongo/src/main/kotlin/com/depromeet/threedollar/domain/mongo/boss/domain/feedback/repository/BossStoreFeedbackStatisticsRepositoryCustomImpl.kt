package com.depromeet.threedollar.domain.mongo.boss.domain.feedback.repository

import java.time.LocalDate
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.gte
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedback

class BossStoreFeedbackStatisticsRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossStoreFeedbackStatisticsRepositoryCustom {

    override fun countAllBossStoreFeedbacks(): Long {
        return mongoTemplate.count(Query(), BossStoreFeedback::class.java)
    }

    override fun countBossStoreFeedbacksBetweenDate(startDate: LocalDate, endDate: LocalDate): Long {
        return mongoTemplate.count(Query()
            .addCriteria(BossStoreFeedback::createdAt
                .gte(startDate.atStartOfDay())
                .lt(endDate.atStartOfDay().plusDays(1))), BossStoreFeedback::class.java)
    }

}
