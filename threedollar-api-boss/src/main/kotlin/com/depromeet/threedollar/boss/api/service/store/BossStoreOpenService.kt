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

    fun openStore(
        bossStoreId: String
    ) {
        BossStoreServiceUtils.validateExistsBossStore(bossStoreRepository, bossStoreId)
        val bossStoreOpenInfo = bossStoreOpenInfoRepository.findByIdOrNull(bossStoreId)
            ?: BossStoreOpenInfo.of(bossStoreId, LocalDateTime.now())
        bossStoreOpenInfoRepository.save(bossStoreOpenInfo);
    }

    fun closeStore(
        bossStoreId: String
    ) {
        BossStoreServiceUtils.validateExistsBossStore(bossStoreRepository, bossStoreId)
        bossStoreOpenInfoRepository.deleteById(bossStoreId)
    }

}
