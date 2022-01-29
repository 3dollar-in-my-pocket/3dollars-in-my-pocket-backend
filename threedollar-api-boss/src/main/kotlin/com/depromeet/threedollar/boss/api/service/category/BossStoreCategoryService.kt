package com.depromeet.threedollar.boss.api.service.category

import com.depromeet.threedollar.boss.api.service.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossStoreCategoryService(
    private val bossStoreCategoryRepository: BossStoreCategoryRepository
) {

    @Transactional(readOnly = true)
    fun getBossStoreCategories(): List<BossStoreCategoryResponse> {
        return bossStoreCategoryRepository.findAll().asSequence()
            .sortedBy { it.sequencePriority }
            .map { BossStoreCategoryResponse.of(it) }
            .toList()
    }

}



