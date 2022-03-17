package com.depromeet.threedollar.domain.mongo.boss.domain.store.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocation
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.where

class BossStoreLocationRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossStoreLocationRepositoryCustom {

    override fun findNearBossStoreLocations(latitude: Double, longitude: Double, maxDistance: Double): List<BossStoreLocation> {
        return mongoTemplate.find(Query()
            .addCriteria(
                where(BossStoreLocation::location)
                    .nearSphere(Point(longitude, latitude))
                    .maxDistance(Distance(maxDistance, Metrics.KILOMETERS).normalizedValue)
            ), BossStoreLocation::class.java
        )
    }

    override fun findBossStoreLocationByBossStoreId(bossStoreId: String): BossStoreLocation? {
        return mongoTemplate.findOne(Query()
            .addCriteria(BossStoreLocation::bossStoreId isEqualTo bossStoreId)
        )
    }

}
