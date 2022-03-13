package com.depromeet.threedollar.api.boss.service.store

import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.boss.domain.store.BossStoreOpenInfoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossStoreRetrieveService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenInfoRepository: BossStoreOpenInfoRepository,
    private val bossStoreLocationRepository: BossStoreLocationRepository
) {

    @Transactional(readOnly = true)
    fun getMyBossStore(bossId: String): BossStoreInfoResponse {
        val bossStore = BossStoreServiceUtils.findBossStoreByBossId(bossStoreRepository, bossId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            location = bossStoreLocationRepository.findBossStoreLocationByBossStoreId(bossStore.id)?.location,
            categories = bossStoreCategoryRepository.findCategoriesByIds(bossStore.categoriesIds),
            bossStoreOpenInfo = bossStoreOpenInfoRepository.findByIdOrNull(bossStore.id)
        )
    }

}
