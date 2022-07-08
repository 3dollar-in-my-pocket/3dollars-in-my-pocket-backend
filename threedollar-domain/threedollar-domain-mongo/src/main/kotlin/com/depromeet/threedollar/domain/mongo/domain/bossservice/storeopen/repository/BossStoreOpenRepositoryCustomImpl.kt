package com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.where
import org.springframework.stereotype.Repository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpen

@Repository
class BossStoreOpenRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate,
) : BossStoreOpenRepositoryCustom {

    override fun findBossOpenStoreByBossStoreId(bossStoreId: String): BossStoreOpen? {
        return mongoTemplate.findOne(Query()
            .addCriteria(BossStoreOpen::bossStoreId isEqualTo bossStoreId)
        )
    }

    override fun existsByBossStoreId(bossStoreId: String): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(BossStoreOpen::bossStoreId isEqualTo bossStoreId), BossStoreOpen::class.java
        )
    }

    override fun findBossOpenStoresByIds(bossStoreIds: List<String>): List<BossStoreOpen> {
        return mongoTemplate.find(Query()
            .addCriteria(where(BossStoreOpen::bossStoreId).`in`(bossStoreIds))
        )
    }

}

