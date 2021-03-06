package com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.repository.statistics

import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.gte
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class BossRegistrationStatisticsRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate,
) : BossRegistrationStatisticsRepositoryCustom {

    override fun countBossRegistrationsBetweenDate(startDate: LocalDate, endDate: LocalDate): Long {
        return mongoTemplate.count(Query()
            .addCriteria(BossRegistration::createdAt
                .gte(startDate.atStartOfDay())
                .lt(endDate.atStartOfDay().plusDays(1))), BossRegistration::class.java)
    }

}
