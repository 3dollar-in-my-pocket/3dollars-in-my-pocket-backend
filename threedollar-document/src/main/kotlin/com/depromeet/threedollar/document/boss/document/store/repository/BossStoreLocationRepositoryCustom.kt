package com.depromeet.threedollar.document.boss.document.store.repository

import com.depromeet.threedollar.document.boss.document.store.BossStoreLocation

interface BossStoreLocationRepositoryCustom {

    fun findNearBossStoreLocations(latitude: Double, longitude: Double, maxDistance: Double): List<BossStoreLocation>

    fun findBossStoreLocationByBossStoreId(bossStoreId: String): BossStoreLocation?

}
