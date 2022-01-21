package com.depromeet.threedollar.boss.api.service.category

import com.depromeet.threedollar.boss.api.service.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.common.exception.model.NotFoundException
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
            .map {
                BossStoreCategoryResponse.of(it)
            }.toList()
    }

}


fun validateExistsCategories(bossStoreCategoryRepository: BossStoreCategoryRepository, categoriesIds: List<String>) {
    val categories = bossStoreCategoryRepository.findCategoriesByIds(categoriesIds)
    if (categories.size != categoriesIds.size) {
        val notExistsCategoriesIds = categoriesIds.subtract(categories.map { it.id })
        throw NotFoundException("해당하는 id (${notExistsCategoriesIds})를 가진 카테고리는 존재하지 않습니다")
    }
}