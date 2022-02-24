package com.depromeet.threedollar.document.boss.document.store.repository

import com.depromeet.threedollar.document.boss.document.store.BossStore
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo

class BossStoreRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossStoreRepositoryCustom {

    override fun findBossStoreByBossId(bossId: String): BossStore? {
        return mongoTemplate.findOne(Query()
            .addCriteria(BossStore::bossId isEqualTo bossId)
        )
    }

    override fun existsBossStoreByIdAndBossId(bossStoreId: String, bossId: String): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(BossStore::id isEqualTo bossStoreId)
            .addCriteria(BossStore::bossId isEqualTo bossId), BossStore::class.java
        )
    }

    override fun existsBossStoreById(bossStoreId: String): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(BossStore::id isEqualTo bossStoreId), BossStore::class.java
        )
    }

}
