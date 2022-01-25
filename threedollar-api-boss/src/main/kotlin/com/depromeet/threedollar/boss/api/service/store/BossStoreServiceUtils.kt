package com.depromeet.threedollar.boss.api.service.store

import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.document.boss.document.store.BossStore
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import org.springframework.data.repository.findByIdOrNull

object BossStoreServiceUtils {

    fun findBossStoreByBossId(bossStoreRepository: BossStoreRepository, bossId: String): BossStore {
        return bossStoreRepository.findBossStoreByBossId(bossId)
            ?: throw NotFoundException("사장님 (${bossId})이 운영중인 가게가 존재하지 않습니다")
    }

    fun validateExistsBossStore(bossStoreRepository: BossStoreRepository, bossStoreId: String) {
        bossStoreRepository.findByIdOrNull(bossStoreId)
            ?: throw NotFoundException("해당하는 ($bossStoreId) 가게는 존재하지 않습니다")
    }

    fun validateBossStoreOwner(bossStoreRepository: BossStoreRepository, bossStoreId: String, bossId: String) {
        bossStoreRepository.findByIdAndBossId(bossStoreId, bossId)
            ?: throw ForbiddenException("해당하는 가게 (${bossStoreId}의 사장님이 아닙니다. bossId: (${bossId})")
    }

}
