package com.depromeet.threedollar.domain.mongo.domain.bossservice.account.repository.statistics

import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.gte
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class BossAccountStatisticsRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate,
) : BossAccountStatisticsRepositoryCustom {

    override fun countBossAccountsBetweenDate(startDate: LocalDate, endDate: LocalDate): Long {
        return mongoTemplate.count(Query()
            .addCriteria(
                BossAccount::createdAt
                    .gte(startDate.atStartOfDay())
                    .lt(endDate.atStartOfDay().plusDays(1))), BossAccount::class.java)
    }

}

