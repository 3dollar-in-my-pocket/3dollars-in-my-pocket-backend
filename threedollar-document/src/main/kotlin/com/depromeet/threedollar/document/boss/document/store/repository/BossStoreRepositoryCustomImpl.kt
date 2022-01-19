package com.depromeet.threedollar.document.boss.document.store.repository

import com.depromeet.threedollar.document.boss.document.store.BossStore
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query

class BossStoreRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossStoreRepositoryCustom {

    override fun findNearBossStores(latitude: Double, longitude: Double, maxDistance: Double): List<BossStore> {
        return mongoTemplate.find(Query(
            where("location")
                .nearSphere(Point(longitude, latitude))
                .maxDistance(Distance(maxDistance, Metrics.KILOMETERS).normalizedValue)
        ), BossStore::class.java)
    }

}
