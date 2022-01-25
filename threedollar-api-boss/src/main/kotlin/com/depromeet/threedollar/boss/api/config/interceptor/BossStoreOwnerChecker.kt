package com.depromeet.threedollar.boss.api.config.interceptor

import com.depromeet.threedollar.boss.api.service.store.BossStoreServiceUtils
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import org.springframework.stereotype.Component

@Component
class BossStoreOwnerChecker(
    private val bossStoreRepository: BossStoreRepository
) {

    fun checkIsOwner(
        pathVariables: Map<String, String>,
        bossId: String
    ) {
        val storeId = pathVariables[PATH_STORE_ID]
            ?: throw IllegalArgumentException("해당하는 가게가 존재하지 않아요 혹은 파라미터로 storeId가 필요합니다")
        BossStoreServiceUtils.validateBossStoreOwner(bossStoreRepository, bossStoreId = storeId, bossId = bossId)
    }

    companion object {
        private const val PATH_STORE_ID = "storeId"
    }

}
