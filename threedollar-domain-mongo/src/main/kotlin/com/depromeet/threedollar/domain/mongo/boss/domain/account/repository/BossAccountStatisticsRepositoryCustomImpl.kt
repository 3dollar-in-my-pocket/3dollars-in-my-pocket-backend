package com.depromeet.threedollar.domain.mongo.boss.domain.account.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.gte
import java.time.LocalDate

class BossAccountStatisticsRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossAccountStatisticsRepositoryCustom {

    override fun countAllBossAccounts(): Long {
        return mongoTemplate.count(Query(), BossAccount::class.java)
    }

    override fun countBossAccountsBetweenDate(startDate: LocalDate, endDate: LocalDate): Long {
        return mongoTemplate.count(Query()
            .addCriteria(BossAccount::createdAt
                .gte(startDate.atStartOfDay())
                .lt(endDate.atStartOfDay().plusDays(1))), BossAccount::class.java)
    }

}
