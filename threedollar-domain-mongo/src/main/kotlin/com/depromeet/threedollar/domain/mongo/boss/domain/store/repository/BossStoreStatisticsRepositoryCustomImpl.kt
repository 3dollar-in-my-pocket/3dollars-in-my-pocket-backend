package com.depromeet.threedollar.domain.mongo.boss.domain.store.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStore
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.gte
import java.time.LocalDate

class BossStoreStatisticsRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossStoreStatisticsRepositoryCustom {

    override fun countAllBossStores(): Long {
        return mongoTemplate.count(Query(), BossStore::class.java)
    }

    override fun countBossStoresBetweenDate(startDate: LocalDate, endDate: LocalDate): Long {
        return mongoTemplate.count(Query()
            .addCriteria(BossStore::createdAt
                .gte(startDate.atStartOfDay())
                .lt(endDate.atStartOfDay().plusDays(1))), BossStore::class.java)
    }

}
