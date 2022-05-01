package com.depromeet.threedollar.domain.mongo.boss.domain.registration.repository

import java.time.LocalDate
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.gte
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistration

class BossRegistrationStatisticsRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossRegistrationStatisticsRepositoryCustom {

    override fun countBossRegistrationsBetweenDate(startDate: LocalDate, endDate: LocalDate): Long {
        return mongoTemplate.count(Query()
            .addCriteria(BossRegistration::createdAt
                .gte(startDate.atStartOfDay())
                .lt(endDate.atStartOfDay().plusDays(1))), BossRegistration::class.java)
    }

}
