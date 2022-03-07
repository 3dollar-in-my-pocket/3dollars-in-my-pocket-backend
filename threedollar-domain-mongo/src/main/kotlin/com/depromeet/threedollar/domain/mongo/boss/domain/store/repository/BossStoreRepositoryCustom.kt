package com.depromeet.threedollar.domain.mongo.boss.domain.store.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStore

interface BossStoreRepositoryCustom {

    fun findBossStoreById(bossStoreId: String): BossStore?

    fun findBossStoreByIdAndBossId(bossStoreId: String, bossId: String): BossStore?

    fun findBossStoreByBossId(bossId: String): BossStore?

    fun existsBossStoreByIdAndBossId(bossStoreId: String, bossId: String): Boolean

    fun existsBossStoreById(bossStoreId: String): Boolean

}
