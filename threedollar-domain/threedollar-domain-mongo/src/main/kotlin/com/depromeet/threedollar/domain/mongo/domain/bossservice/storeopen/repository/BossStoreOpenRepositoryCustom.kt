package com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.repository

import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpen

interface BossStoreOpenRepositoryCustom {

    fun findBossOpenStoreByBossStoreId(bossStoreId: String): BossStoreOpen?

    fun existsByBossStoreId(bossStoreId: String): Boolean

    fun findBossOpenStoresByIds(bossStoreIds: List<String>): List<BossStoreOpen>

}
