package com.depromeet.threedollar.boss.api.service.store

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.document.boss.document.store.BossStore
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository

object BossStoreServiceUtils {

    fun findBossStoreById(bossStoreRepository: BossStoreRepository, bossStoreId: String): BossStore {
        return bossStoreRepository.findActiveBossStoreById(bossStoreId)
            ?: throw NotFoundException("해당하는 가게 (${bossStoreId})는 존재하지 않습니다", ErrorCode.NOTFOUND_STORE)
    }

    fun findBossStoreByBossId(bossStoreRepository: BossStoreRepository, bossId: String): BossStore {
        return bossStoreRepository.findActiveBossStoreByBossId(bossId)
            ?: throw NotFoundException("사장님 (${bossId})이 운영중인 가게가 존재하지 않습니다", ErrorCode.NOTFOUND_BOSS_OWNED_STORE)
    }

    fun validateExistsBossStore(bossStoreRepository: BossStoreRepository, bossStoreId: String) {
        if (!bossStoreRepository.existsActiveBossStoreById(bossStoreId = bossStoreId)) {
            throw NotFoundException("해당하는 가게(${bossStoreId})는 존재하지 않습니다", ErrorCode.NOTFOUND_STORE)
        }
    }

    fun validateExistsBossStoreByBoss(bossStoreRepository: BossStoreRepository, bossStoreId: String, bossId: String) {
        if (!bossStoreRepository.existsActiveBossStoreByIdAndBossId(bossStoreId = bossStoreId, bossId = bossId)) {
            throw NotFoundException("사장님($bossStoreId)이 운영중인 ($bossStoreId) 가게는 존재하지 않습니다", ErrorCode.NOTFOUND_STORE)
        }
    }

}
