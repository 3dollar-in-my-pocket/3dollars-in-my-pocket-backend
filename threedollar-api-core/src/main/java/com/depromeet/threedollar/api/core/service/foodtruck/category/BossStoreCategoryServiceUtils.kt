package com.depromeet.threedollar.api.core.service.foodtruck.category

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.category.BossStoreCategoryRepository

object BossStoreCategoryServiceUtils {

    fun validateExistsCategories(bossStoreCategoryRepository: BossStoreCategoryRepository, categoriesIds: Set<String>) {
        val categories = bossStoreCategoryRepository.findAllCategoriesByIds(categoriesIds)
        if (categories.size != categoriesIds.size) {
            val notExistsCategoriesIds = categoriesIds.subtract(categories.asSequence().map { it.id }.toSet())
            throw NotFoundException("해당하는 푸드트럭 카테고리(${notExistsCategoriesIds})는 존재하지 않습니다", ErrorCode.NOTFOUND_CATEGORY)
        }
    }

    fun validateExistsCategory(bossStoreCategoryRepository: BossStoreCategoryRepository, categoriesId: String) {
        if (!bossStoreCategoryRepository.existsById(categoriesId)) {
            throw NotFoundException("해당하는 푸드트럭 카테고리(${categoriesId})는 존재하지 않습니다", ErrorCode.NOTFOUND_CATEGORY)
        }
    }

}
