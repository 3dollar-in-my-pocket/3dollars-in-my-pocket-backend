package com.depromeet.threedollar.domain.mongo.foodtruck.domain.store.repository

import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.inValues
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.where
import org.springframework.stereotype.Repository
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.store.BossStore

@Repository
class BossStoreRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate,
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

    override fun findAllNearBossStoresFilterByCategoryId(latitude: Double, longitude: Double, categoryId: String?, maxDistance: Double, size: Int): List<BossStore> {
        if (categoryId == null) {
            return findAllAroundBossStores(latitude = latitude, longitude = longitude, maxDistance = maxDistance, size = size)
        }
        return findAllAroundBossStoresByCategoryId(latitude = latitude, longitude = longitude, categoryId = categoryId, maxDistance = maxDistance, size = size)
    }

    private fun findAllAroundBossStores(latitude: Double, longitude: Double, maxDistance: Double, size: Int): List<BossStore> {
        return mongoTemplate.find(Query()
            .addCriteria(
                where(BossStore::location)
                    .nearSphere(Point(longitude, latitude))
                    .maxDistance(Distance(maxDistance, Metrics.KILOMETERS).normalizedValue)
            )
            .limit(size), BossStore::class.java)
    }

    private fun findAllAroundBossStoresByCategoryId(latitude: Double, longitude: Double, categoryId: String, maxDistance: Double, size: Int): List<BossStore> {
        return mongoTemplate.find(Query()
            .addCriteria(
                where(BossStore::location)
                    .nearSphere(Point(longitude, latitude))
                    .maxDistance(Distance(maxDistance, Metrics.KILOMETERS).normalizedValue)
            )
            .addCriteria(where(BossStore::categoriesIds).`in`(categoryId))
            .limit(size), BossStore::class.java)
    }

}

