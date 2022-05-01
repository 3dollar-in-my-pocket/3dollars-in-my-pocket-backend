package com.depromeet.threedollar.domain.mongo.boss.domain.store.repository

import java.time.LocalDate
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.gte
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStore

class BossStoreLocationStatisticsRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossStoreLocationStatisticsRepositoryCustom {

    override fun countUpdatedBossStoreLocationsBetweenDate(startDate: LocalDate, endDate: LocalDate): Long {
        return mongoTemplate.count(Query()
            .addCriteria(BossStore::updatedAt.gte(startDate.atStartOfDay()).lt(endDate.atStartOfDay().plusDays(1))), BossStore::class.java)
    }

}
