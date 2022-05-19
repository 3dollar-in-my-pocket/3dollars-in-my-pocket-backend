package com.depromeet.threedollar.api.boss.service.store

import java.time.LocalDateTime
import org.springframework.stereotype.Service
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.boss.store.BossStoreOpenTimeRepository

@Service
class BossStoreOpenService(
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository,
    private val bossStoreRepository: BossStoreRepository,
) {

    fun openBossStore(bossStoreId: String, bossId: String, mapLocation: LocationValue) {
        val bossStore = BossStoreServiceUtils.findBossStoreByIdAndBossId(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        mapLocation.let {
            if (bossStore.hasChangedLocation(latitude = it.latitude, longitude = it.longitude)) {
                bossStore.updateLocation(latitude = it.latitude, longitude = it.longitude)
                bossStoreRepository.save(bossStore)
            }
            upsertStoreOpenInfo(bossStoreId = bossStoreId)
        }
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
