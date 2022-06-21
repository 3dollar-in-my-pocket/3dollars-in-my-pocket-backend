package com.depromeet.threedollar.api.bossservice.service.store

import java.time.LocalDateTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.core.service.bossservice.store.BossStoreServiceHelper
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.response.BossStoreOpenStatusResponse
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.store.BossStoreOpenTimeRepository

@Service
class BossStoreOpenService(
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository,
    private val bossStoreRepository: BossStoreRepository,
) {

    @Transactional
    fun openBossStore(bossStoreId: String, bossId: String, mapLocation: LocationValue): BossStoreOpenStatusResponse {
        val bossStore = BossStoreServiceHelper.findBossStoreByIdAndBossId(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        bossStore.updateLocation(latitude = mapLocation.latitude, longitude = mapLocation.longitude)
        bossStoreRepository.save(bossStore)

        val openDateTime = LocalDateTime.now()
        bossStoreOpenTimeRepository.set(bossStoreId = bossStoreId, openDateTime = openDateTime)
        return BossStoreOpenStatusResponse.open(openDateTime)
    }

    @Transactional
    fun renewBossStoreOpenInfo(bossStoreId: String, bossId: String, mapLocation: LocationValue): BossStoreOpenStatusResponse {
        val bossStore = BossStoreServiceHelper.findBossStoreByIdAndBossId(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        validateIsOpenStore(bossStoreId = bossStoreId)
        if (bossStore.hasChangedLocation(latitude = mapLocation.latitude, longitude = mapLocation.longitude)) {
            if (!bossStore.canBeRenewOpenDistance(mapLocation)) {
                bossStoreOpenTimeRepository.delete(bossStoreId = bossStoreId)
                return BossStoreOpenStatusResponse.close()
            }
            bossStore.updateLocation(latitude = mapLocation.latitude, longitude = mapLocation.longitude)
            bossStoreRepository.save(bossStore)
        }

        val openDateTime = upsertStoreOpenInfo(bossStoreId = bossStoreId)
        return BossStoreOpenStatusResponse.open(openDateTime)
    }

    private fun validateIsOpenStore(bossStoreId: String) {
        if (!bossStoreOpenTimeRepository.exists(bossStoreId)) {
            throw ForbiddenException("현재 오픈중인 가게(${bossStoreId})가 아닙니다.", ErrorCode.FORBIDDEN_NOT_OPEN_STORE)
        }
    }

    private fun upsertStoreOpenInfo(bossStoreId: String): LocalDateTime {
        val openDateTime: LocalDateTime = bossStoreOpenTimeRepository.get(bossStoreId)
            ?: LocalDateTime.now()
        bossStoreOpenTimeRepository.set(bossStoreId = bossStoreId, openDateTime = openDateTime)
        return openDateTime
    }

    @Transactional
    fun closeBossStore(bossStoreId: String, bossId: String): BossStoreOpenStatusResponse {
        BossStoreServiceHelper.validateExistsBossStoreByBoss(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        bossStoreOpenTimeRepository.delete(bossStoreId = bossStoreId)
        return BossStoreOpenStatusResponse.close()
    }

}
