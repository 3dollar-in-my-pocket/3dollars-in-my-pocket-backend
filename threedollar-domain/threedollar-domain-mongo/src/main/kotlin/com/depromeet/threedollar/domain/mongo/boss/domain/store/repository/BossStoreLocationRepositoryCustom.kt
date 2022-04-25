package com.depromeet.threedollar.domain.mongo.boss.domain.store.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocation

interface BossStoreLocationRepositoryCustom {

    fun findAllNearBossStoreLocations(latitude: Double, longitude: Double, maxDistance: Double, limit: Int): List<BossStoreLocation>

    fun findBossStoreLocationByBossStoreId(bossStoreId: String): BossStoreLocation?

}
