package com.depromeet.threedollar.document.boss.document.store.repository

import com.depromeet.threedollar.document.boss.document.store.BossStore
import com.depromeet.threedollar.document.boss.document.store.BossStoreStatus
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo

class BossStoreRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossStoreRepositoryCustom {

    override fun findActiveBossStoreById(bossStoreId: String): BossStore? {
        return mongoTemplate.findOne(Query()
            .addCriteria(BossStore::id isEqualTo bossStoreId)
            .addCriteria(BossStore::status isEqualTo BossStoreStatus.ACTIVE)
        )
    }

    override fun findActiveBossStoreByBossId(bossId: String): BossStore? {
        return mongoTemplate.findOne(Query()
            .addCriteria(BossStore::bossId isEqualTo bossId)
            .addCriteria(BossStore::status isEqualTo BossStoreStatus.ACTIVE)
        )
    }

    override fun existsActiveBossStoreByIdAndBossId(bossStoreId: String, bossId: String): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(BossStore::id isEqualTo bossStoreId)
            .addCriteria(BossStore::bossId isEqualTo bossId)
            .addCriteria(BossStore::status isEqualTo BossStoreStatus.ACTIVE), BossStore::class.java
        )
    }

    override fun existsActiveBossStoreById(bossStoreId: String): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(BossStore::id isEqualTo bossStoreId)
            .addCriteria(BossStore::status isEqualTo BossStoreStatus.ACTIVE), BossStore::class.java
        )
    }

}
