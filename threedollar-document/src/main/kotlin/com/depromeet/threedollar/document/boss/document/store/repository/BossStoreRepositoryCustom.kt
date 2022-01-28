package com.depromeet.threedollar.document.boss.document.store.repository

import com.depromeet.threedollar.document.boss.document.store.BossStore

interface BossStoreRepositoryCustom {

    fun findNearBossStores(latitude: Double, longitude: Double, maxDistance: Double): List<BossStore>

    fun findBossStoreByBossId(bossId: String): BossStore?

    fun findBossStoreByIdAndBossId(bossStoreId: String, bossId: String): BossStore?

}
