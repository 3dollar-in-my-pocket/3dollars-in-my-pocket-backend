package com.depromeet.threedollar.boss.api.service.store

import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import com.depromeet.threedollar.redis.boss.domain.store.BossStoreOpenInfo
import com.depromeet.threedollar.redis.boss.domain.store.BossStoreOpenInfoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BossStoreOpenService(
    private val bossStoreOpenInfoRepository: BossStoreOpenInfoRepository,
    private val bossStoreRepository: BossStoreRepository
) {

    fun openBossStore(bossStoreId: String, bossId: String) {
        BossStoreServiceUtils.validateExistsBossStoreByBoss(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        val bossStoreOpenInfo = bossStoreOpenInfoRepository.findByIdOrNull(bossStoreId)
            ?: BossStoreOpenInfo.of(bossStoreId, LocalDateTime.now())
        bossStoreOpenInfoRepository.save(bossStoreOpenInfo);
    }

    fun closeBossStore(bossStoreId: String, bossId: String) {
        BossStoreServiceUtils.validateExistsBossStoreByBoss(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        bossStoreOpenInfoRepository.deleteById(bossStoreId)
    }

}
