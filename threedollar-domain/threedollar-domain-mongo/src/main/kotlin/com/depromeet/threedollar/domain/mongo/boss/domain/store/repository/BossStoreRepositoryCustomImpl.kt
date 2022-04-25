package com.depromeet.threedollar.domain.mongo.boss.domain.store.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStore
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.inValues
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.where

class BossStoreRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossStoreRepositoryCustom {

    override fun findBossStoreById(bossStoreId: String): BossStore? {
        return mongoTemplate.findOne(Query()
            .addCriteria(BossStore::id isEqualTo bossStoreId)
        )
    }

    override fun findBossStoreByIdAndBossId(bossStoreId: String, bossId: String): BossStore? {
        return mongoTemplate.findOne(Query()
            .addCriteria(BossStore::id isEqualTo bossStoreId)
            .addCriteria(BossStore::bossId isEqualTo bossId)
        )
    }

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

    override fun findAllByIdByCategory(bossStoreIds: List<String>, categoryId: String?): List<BossStore> {
        return categoryId?.let {
            mongoTemplate.find(Query()
                .addCriteria(BossStore::id inValues bossStoreIds)
                .addCriteria(where(BossStore::categoriesIds).`in`(categoryId)), BossStore::class.java
            )
        } ?: findAllById(bossStoreIds)
    }

    private fun findAllById(bossStoreIds: List<String>): List<BossStore> {
        return mongoTemplate.find(Query()
            .addCriteria(BossStore::id inValues bossStoreIds), BossStore::class.java)
    }

}

