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

    /**
     * latitude, longitude 하나 이상 null일 경우 (오픈을 한번도 한적이 없는 가게인경우)
     * - 실행계획에서 아예 필터링 되는지 체크할 필요가 있음.
     */
    override fun findNearBossStores(latitude: Double, longitude: Double, maxDistance: Double): List<BossStore> {
        return mongoTemplate.find(
            query(
                where("location").nearSphere(Point(longitude, latitude))
                    .maxDistance(Distance(maxDistance, Metrics.KILOMETERS).normalizedValue)
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

    override fun existsBossStoreByIdAndBossId(bossStoreId: String, bossId: String): Boolean {
        return mongoTemplate.exists(
            Query(
                where("bossId").`is`(bossId)
            ), BossStore::class.java
        )
    }

}
