package com.depromeet.threedollar.api.boss.service.store

import java.time.LocalDateTime
import org.springframework.stereotype.Service
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocation
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.boss.store.BossStoreOpenTimeRepository

@Service
class BossStoreOpenService(
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreLocationRepository: BossStoreLocationRepository,
) {

    fun openBossStore(bossStoreId: String, bossId: String, mapLocation: LocationValue) {
        BossStoreServiceUtils.validateExistsBossStoreByBoss(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        updateStoreLocation(bossStoreId, latitude = mapLocation.latitude, longitude = mapLocation.longitude)
        upsertStoreOpenInfo(bossStoreId = bossStoreId)
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
        val openDateTime: LocalDateTime = bossStoreOpenTimeRepository.get(bossStoreId)
            ?: LocalDateTime.now()
        bossStoreOpenTimeRepository.set(bossStoreId = bossStoreId, openDateTime = openDateTime)
    }

    fun closeBossStore(bossStoreId: String, bossId: String) {
        BossStoreServiceUtils.validateExistsBossStoreByBoss(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        bossStoreOpenTimeRepository.delete(bossStoreId = bossStoreId)
    }

}
