package com.depromeet.threedollar.domain.mongo.boss.domain.store.repository

import java.time.LocalDate
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.gte
import org.springframework.stereotype.Repository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossDeletedStore

@Repository
class BossDeletedStoreStatisticsRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate,
) : BossDeletedStoreStatisticsRepositoryCustom {

    override fun countDeletedBossStoresBetweenDate(startDate: LocalDate, endDate: LocalDate): Long {
        return mongoTemplate.count(Query()
            .addCriteria(BossDeletedStore::createdAt.gte(startDate.atStartOfDay()).lt(endDate.atStartOfDay().plusDays(1))), BossDeletedStore::class.java)
    }

}
