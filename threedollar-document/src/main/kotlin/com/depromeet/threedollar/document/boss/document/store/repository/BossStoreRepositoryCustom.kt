package com.depromeet.threedollar.document.boss.document.store.repository

import com.depromeet.threedollar.document.boss.document.store.BossStore

interface BossStoreRepositoryCustom {

    fun findActiveBossStoreById(bossStoreId: String): BossStore?

    fun findActiveBossStoreByBossId(bossId: String): BossStore?

    fun existsActiveBossStoreByIdAndBossId(bossStoreId: String, bossId: String): Boolean

    fun existsActiveBossStoreById(bossStoreId: String): Boolean

}
