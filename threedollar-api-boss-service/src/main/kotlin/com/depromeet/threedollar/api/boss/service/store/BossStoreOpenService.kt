package com.depromeet.threedollar.api.boss.service.store

import java.time.LocalDateTime
import org.springframework.stereotype.Service
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.store.BossStoreOpenTimeRepository

@Service
class BossStoreOpenService(
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository,
    private val bossStoreRepository: BossStoreRepository,
) {

    fun openBossStore(bossStoreId: String, bossId: String, mapLocation: LocationValue) {
        val bossStore = BossStoreServiceUtils.findBossStoreByIdAndBossId(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        if (bossStore.hasChangedLocation(latitude = mapLocation.latitude, longitude = mapLocation.longitude)) {
            bossStore.updateLocation(latitude = mapLocation.latitude, longitude = mapLocation.longitude)
            bossStoreRepository.save(bossStore)
        }
        upsertStoreOpenInfo(bossStoreId = bossStoreId)
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
