package com.depromeet.threedollar.domain.mongo.boss.domain.store.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocation

interface BossStoreLocationRepositoryCustom {

    fun findNearBossStoreLocations(latitude: Double, longitude: Double, maxDistance: Double): List<BossStoreLocation>

    fun findBossStoreLocationByBossStoreId(bossStoreId: String): BossStoreLocation?

}