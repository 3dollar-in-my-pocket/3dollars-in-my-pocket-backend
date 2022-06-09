package com.depromeet.threedollar.domain.mongo.domain.bossservice.store.repository.statistics

import java.time.LocalDate
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.gte
import org.springframework.stereotype.Repository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore

@Repository
class BossStoreStatisticsRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate,
) : BossStoreStatisticsRepositoryCustom {

    override fun countBossStoresBetweenDate(startDate: LocalDate, endDate: LocalDate): Long {
        return mongoTemplate.count(Query()
            .addCriteria(BossStore::createdAt.gte(startDate.atStartOfDay()).lt(endDate.atStartOfDay().plusDays(1))), BossStore::class.java)
    }

}
