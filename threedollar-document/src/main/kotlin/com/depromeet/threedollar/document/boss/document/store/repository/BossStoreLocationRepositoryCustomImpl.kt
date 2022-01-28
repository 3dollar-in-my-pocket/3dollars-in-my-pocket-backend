package com.depromeet.threedollar.document.boss.document.store.repository

import com.depromeet.threedollar.document.boss.document.store.BossStoreLocation
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class BossStoreLocationRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossStoreLocationRepositoryCustom {

    override fun findNearBossStoreLocations(latitude: Double, longitude: Double, maxDistance: Double): List<BossStoreLocation> {
        return mongoTemplate.find(
            Query.query(
                Criteria.where("location").nearSphere(Point(longitude, latitude))
                    .maxDistance(Distance(maxDistance, Metrics.KILOMETERS).normalizedValue)
            ), BossStoreLocation::class.java
        )
    }

    override fun findBossStoreLocationByBossStoreId(bossStoreId: String): BossStoreLocation? {
        return mongoTemplate.findOne(
            Query(
                Criteria.where("bossStoreId").`is`(bossStoreId)
            ), BossStoreLocation::class.java
        )
    }

}
