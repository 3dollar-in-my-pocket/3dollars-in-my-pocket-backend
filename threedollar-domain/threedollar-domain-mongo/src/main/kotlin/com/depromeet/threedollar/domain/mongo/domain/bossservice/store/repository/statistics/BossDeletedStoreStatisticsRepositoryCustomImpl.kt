package com.depromeet.threedollar.domain.mongo.domain.bossservice.store.repository.statistics

import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossDeletedStore
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.gte
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class BossDeletedStoreStatisticsRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate,
) : BossDeletedStoreStatisticsRepositoryCustom {

    override fun countDeletedBossStoresBetweenDate(startDate: LocalDate, endDate: LocalDate): Long {
        return mongoTemplate.count(Query()
            .addCriteria(BossDeletedStore::createdAt.gte(startDate.atStartOfDay()).lt(endDate.atStartOfDay().plusDays(1))), BossDeletedStore::class.java)
    }

}
