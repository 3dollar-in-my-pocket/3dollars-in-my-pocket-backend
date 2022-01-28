package com.depromeet.threedollar.boss.api.service.store

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.document.boss.document.store.BossStore
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import org.springframework.data.repository.findByIdOrNull

object BossStoreServiceUtils {

    fun findBossStoreById(bossStoreRepository: BossStoreRepository, bossStoreId: String): BossStore {
        return bossStoreRepository.findByIdOrNull(bossStoreId)
            ?: throw NotFoundException("해당하는 가게 (${bossStoreId})는 존재하지 않습니다", ErrorCode.NOT_FOUND_STORE_EXCEPTION)
    }

    fun findBossStoreByBossId(bossStoreRepository: BossStoreRepository, bossId: String): BossStore {
        return bossStoreRepository.findBossStoreByBossId(bossId)
            ?: throw NotFoundException("사장님 (${bossId})이 운영중인 가게가 존재하지 않습니다", ErrorCode.NOT_FOUND_BOSS_OWNED_STORE_EXCEPTION)
    }

    fun validateExistsBossStoreByBoss(bossStoreRepository: BossStoreRepository, bossStoreId: String, bossId: String) {
        bossStoreRepository.findBossStoreByIdAndBossId(
            bossStoreId = bossStoreId,
            bossId = bossId
        )
            ?: throw NotFoundException("사장님($bossStoreId)이 운영중인 ($bossStoreId) 가게는 존재하지 않습니다", ErrorCode.NOT_FOUND_STORE_EXCEPTION)
    }

}