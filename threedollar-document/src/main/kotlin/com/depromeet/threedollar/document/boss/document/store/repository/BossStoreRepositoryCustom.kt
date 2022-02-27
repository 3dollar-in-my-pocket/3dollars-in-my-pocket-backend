package com.depromeet.threedollar.document.boss.document.store.repository

import com.depromeet.threedollar.document.boss.document.store.BossStore

interface BossStoreRepositoryCustom {

    fun findBossStoreById(bossStoreId: String): BossStore?

    fun findBossStoreByIdAndBossId(bossStoreId: String, bossId: String): BossStore?

    fun findBossStoreByBossId(bossId: String): BossStore?

    fun existsBossStoreByIdAndBossId(bossStoreId: String, bossId: String): Boolean

    fun existsBossStoreById(bossStoreId: String): Boolean

}
