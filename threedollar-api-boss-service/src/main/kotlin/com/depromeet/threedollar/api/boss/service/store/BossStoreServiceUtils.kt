package com.depromeet.threedollar.api.boss.service.store

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository

object BossStoreServiceUtils {

    fun findBossStoreByIdAndBossId(
        bossStoreRepository: BossStoreRepository,
        bossStoreId: String,
        bossId: String,
    ): BossStore {
        return bossStoreRepository.findBossStoreByIdAndBossId(bossStoreId = bossStoreId, bossId = bossId)
            ?: throw NotFoundException("해당하는 사장님 가게(${bossStoreId})는 존재하지 않습니다", ErrorCode.NOT_FOUND_STORE)
    }

    fun findBossStoreByBossId(
        bossStoreRepository: BossStoreRepository,
        bossId: String,
    ): BossStore {
        return bossStoreRepository.findBossStoreByBossId(bossId)
            ?: throw NotFoundException("사장님(${bossId})이 운영중인 사장님 가게가 존재하지 않습니다", ErrorCode.NOT_FOUND_STORE)
    }

    fun validateExistsBossStoreByBoss(
        bossStoreRepository: BossStoreRepository,
        bossStoreId: String,
        bossId: String,
    ) {
        if (!bossStoreRepository.existsBossStoreByIdAndBossId(bossStoreId = bossStoreId, bossId = bossId)) {
            throw NotFoundException("사장님($bossStoreId)이 운영중인 사장님 가게($bossStoreId)는 존재하지 않습니다", ErrorCode.NOT_FOUND_STORE)
        }
    }

}
