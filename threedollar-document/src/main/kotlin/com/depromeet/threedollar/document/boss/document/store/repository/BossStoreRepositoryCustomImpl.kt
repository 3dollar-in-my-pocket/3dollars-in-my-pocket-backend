package com.depromeet.threedollar.document.boss.document.store.repository

import com.depromeet.threedollar.document.boss.document.store.BossStore
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Query.query

class BossStoreRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossStoreRepositoryCustom {

    override fun findNearBossStores(latitude: Double, longitude: Double, maxDistance: Double): List<BossStore> {
        return mongoTemplate.find(
            query(
                where("location").nearSphere(Point(longitude, latitude))
                    .maxDistance(Distance(maxDistance, Metrics.KILOMETERS).normalizedValue)
            ), BossStore::class.java
        )
    }

    override fun findByIdAndBossId(bossStoreId: String, bossId: String): BossStore? {
        return mongoTemplate.findOne(
            query(
                where("_id").`is`(bossStoreId)
            ).addCriteria(
                where("bossId").`is`(bossId)
            ), BossStore::class.java
        )
    }

    override fun findBossStoreByBossId(bossId: String): BossStore? {
        return mongoTemplate.findOne(
            Query(
                where("bossId").`is`(bossId)
            ), BossStore::class.java
        )
    }

}
