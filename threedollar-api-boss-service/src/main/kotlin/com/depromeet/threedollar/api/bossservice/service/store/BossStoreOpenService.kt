package com.depromeet.threedollar.api.bossservice.service.store

import com.depromeet.threedollar.api.core.service.bossservice.store.BossStoreServiceHelper
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.domain.mongo.config.mongo.MongoTransactional
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpen
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpenRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BossStoreOpenService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreOpenRepository: BossStoreOpenRepository,
) {

    @MongoTransactional
    fun openBossStore(bossStoreId: String, bossId: String, mapLocation: LocationValue) {
        val bossStore = BossStoreServiceHelper.findBossStoreByIdAndBossId(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        bossStore.updateLocation(latitude = mapLocation.latitude, longitude = mapLocation.longitude)
        bossStoreRepository.save(bossStore)
        upsertBossOpenStore(bossStoreId)
    }

    private fun upsertBossOpenStore(bossStoreId: String) {
        val bossStoreOpen: BossStoreOpen? = bossStoreOpenRepository.findBossOpenStoreByBossStoreId(bossStoreId = bossStoreId)
        if (bossStoreOpen == null) {
            val newBossStoreOpen = BossStoreOpen.of(bossStoreId = bossStoreId, dateTime = LocalDateTime.now())
            bossStoreOpenRepository.save(newBossStoreOpen)
            return
        }
        bossStoreOpen.updateExpiredAt(LocalDateTime.now())
        bossStoreOpenRepository.save(bossStoreOpen)
    }

    @MongoTransactional
    fun renewBossStoreOpenInfo(bossStoreId: String, bossId: String, mapLocation: LocationValue) {
        val bossStore = BossStoreServiceHelper.findBossStoreByIdAndBossId(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        val bossStoreOpen = bossStoreOpenRepository.findBossOpenStoreByBossStoreId(bossStoreId = bossStoreId)
            ?: throw ForbiddenException("현재 오픈중인 가게(${bossStoreId})가 아닙니다.", ErrorCode.FORBIDDEN_NOT_OPEN_STORE)

        if (bossStore.hasChangedLocation(latitude = mapLocation.latitude, longitude = mapLocation.longitude)) {
            bossStore.updateLocation(latitude = mapLocation.latitude, longitude = mapLocation.longitude)
            bossStoreRepository.save(bossStore)
        }

        bossStoreOpen.updateExpiredAt(LocalDateTime.now())
        bossStoreOpenRepository.save(bossStoreOpen)
    }

    fun closeBossStore(bossStoreId: String, bossId: String) {
        BossStoreServiceHelper.validateExistsBossStoreByBoss(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        val bossStoreOpen: BossStoreOpen? = bossStoreOpenRepository.findBossOpenStoreByBossStoreId(bossStoreId = bossStoreId)
        bossStoreOpen?.let {
            bossStoreOpenRepository.delete(it)
        }
    }

}
