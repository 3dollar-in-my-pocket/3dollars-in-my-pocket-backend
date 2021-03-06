package com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.repository.statistics

import com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.BossStoreFeedback
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.gte
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class BossStoreFeedbackStatisticsRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate,
) : BossStoreFeedbackStatisticsRepositoryCustom {

    override fun countBossStoreFeedbacksBetweenDate(startDate: LocalDate, endDate: LocalDate): Long {
        return mongoTemplate.count(Query()
            .addCriteria(BossStoreFeedback::createdAt
                .gte(startDate.atStartOfDay())
                .lt(endDate.atStartOfDay().plusDays(1))), BossStoreFeedback::class.java)
    }

}
