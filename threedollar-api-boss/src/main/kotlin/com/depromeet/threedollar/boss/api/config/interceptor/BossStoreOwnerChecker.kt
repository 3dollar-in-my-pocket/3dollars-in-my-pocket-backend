package com.depromeet.threedollar.boss.api.config.interceptor

import com.depromeet.threedollar.common.exception.model.ForbiddenException
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
        validateBossStoreOwner(bossStoreRepository, bossStoreId = storeId, bossId = bossId)
    }

    companion object {
        private const val PATH_STORE_ID = "storeId"
    }

}

private fun validateBossStoreOwner(bossStoreRepository: BossStoreRepository, bossStoreId: String, bossId: String) {
    bossStoreRepository.findByIdAndBossId(bossStoreId, bossId)
        ?: throw ForbiddenException("해당하는 가게 (${bossStoreId}의 사장님이 아닙니다. bossId: (${bossId})")
}
