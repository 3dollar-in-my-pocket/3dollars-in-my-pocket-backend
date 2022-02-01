package com.depromeet.threedollar.document.boss.document.store.repository

import com.depromeet.threedollar.document.boss.document.store.BossStore

interface BossStoreRepositoryCustom {

    fun findBossStoreByBossId(bossId: String): BossStore?

    fun existsBossStoreByIdAndBossId(bossStoreId: String, bossId: String): Boolean

}
