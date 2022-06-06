package com.depromeet.threedollar.api.core.service.bossservice.store

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository

object BossStoreCommonServiceUtils {

    fun findBossStoreById(bossStoreRepository: BossStoreRepository, bossStoreId: String): BossStore {
        return bossStoreRepository.findBossStoreById(bossStoreId)
            ?: throw NotFoundException("해당하는 사장님 가게 (${bossStoreId})는 존재하지 않습니다", ErrorCode.NOT_FOUND_STORE)
    }

    fun validateExistsBossStore(bossStoreRepository: BossStoreRepository, bossStoreId: String) {
        if (!bossStoreRepository.existsBossStoreById(bossStoreId = bossStoreId)) {
            throw NotFoundException("해당하는 사장님 가게(${bossStoreId})는 존재하지 않습니다", ErrorCode.NOT_FOUND_STORE)
        }
    }

}
