package com.depromeet.threedollar.domain.mongo.domain.bossservice.store.repository

import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore

interface BossStoreRepositoryCustom {

    fun findBossStoreById(bossStoreId: String): BossStore?

    fun findBossStoreByIdAndBossId(bossStoreId: String, bossId: String): BossStore?

    fun findBossStoreByBossId(bossId: String): BossStore?

    fun existsBossStoreByIdAndBossId(bossStoreId: String, bossId: String): Boolean

    fun existsBossStoreById(bossStoreId: String): Boolean

    fun findAllByIdByCategory(bossStoreIds: List<String>, categoryId: String?): List<BossStore>

    fun findAllNearBossStoresFilterByCategoryId(latitude: Double, longitude: Double, categoryId: String?, maxDistance: Double, size: Int): List<BossStore>

}
