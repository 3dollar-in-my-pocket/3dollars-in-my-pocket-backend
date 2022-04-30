package com.depromeet.threedollar.domain.mongo.boss.domain.store.repository

import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.where
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocation

class BossStoreLocationRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossStoreLocationRepositoryCustom {

    override fun findAllNearBossStoreLocations(latitude: Double, longitude: Double, maxDistance: Double, size: Int): List<BossStoreLocation> {
        return mongoTemplate.find(Query()
            .addCriteria(
                where(BossStoreLocation::location)
                    .nearSphere(Point(longitude, latitude))
                    .maxDistance(Distance(maxDistance, Metrics.KILOMETERS).normalizedValue)
            ).limit(size), BossStoreLocation::class.java
        )
    }

    override fun findBossStoreLocationByBossStoreId(bossStoreId: String): BossStoreLocation? {
        return mongoTemplate.findOne(Query()
            .addCriteria(BossStoreLocation::bossStoreId isEqualTo bossStoreId)
        )
    }

}
