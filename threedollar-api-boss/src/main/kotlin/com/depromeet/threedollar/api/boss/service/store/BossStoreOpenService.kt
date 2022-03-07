package com.depromeet.threedollar.api.boss.service.store

import com.depromeet.threedollar.common.model.CoordinateValue
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocation
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.boss.domain.store.BossStoreOpenInfo
import com.depromeet.threedollar.domain.redis.boss.domain.store.BossStoreOpenInfoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BossStoreOpenService(
        private val bossStoreOpenInfoRepository: BossStoreOpenInfoRepository,
        private val bossStoreRepository: BossStoreRepository,
        private val bossStoreLocationRepository: BossStoreLocationRepository
) {

    fun openBossStore(bossStoreId: String, bossId: String, mapCoordinate: CoordinateValue) {
        BossStoreServiceUtils.validateExistsBossStoreByBoss(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        updateStoreLocation(bossStoreId, latitude = mapCoordinate.latitude, longitude = mapCoordinate.longitude)
        upsertStoreOpenInfo(bossStoreId)
    }

    private fun updateStoreLocation(bossStoreId: String, latitude: Double, longitude: Double) {
        val bossStoreLocation = bossStoreLocationRepository.findBossStoreLocationByBossStoreId(bossStoreId)
        bossStoreLocation?.let {
            if (it.hasChangedLocation(latitude = latitude, longitude = longitude)) {
                it.updateLocation(latitude = latitude, longitude = longitude)
                bossStoreLocationRepository.save(it)
            }
        }
            ?: bossStoreLocationRepository.save(BossStoreLocation.of(bossStoreId = bossStoreId, latitude = latitude, longitude = longitude))
    }

    private fun upsertStoreOpenInfo(bossStoreId: String) {
        val bossStoreOpenInfo = bossStoreOpenInfoRepository.findByIdOrNull(bossStoreId)
            ?: BossStoreOpenInfo.of(bossStoreId, LocalDateTime.now())
        bossStoreOpenInfoRepository.save(bossStoreOpenInfo)
    }

    fun closeBossStore(bossStoreId: String, bossId: String) {
        BossStoreServiceUtils.validateExistsBossStoreByBoss(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        bossStoreOpenInfoRepository.deleteById(bossStoreId)
    }

}
