package com.depromeet.threedollar.api.core.service.boss.category

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository

object BossStoreCategoryServiceUtils {

    fun validateExistsCategories(bossStoreCategoryRepository: BossStoreCategoryRepository, categoriesIds: Set<String>) {
        val categories = bossStoreCategoryRepository.findAllCategoriesByIds(categoriesIds)
        if (categories.size != categoriesIds.size) {
            val categoriesSet = categories.asSequence()
                .map { category -> category.id }
                .toSet()
            val notExistsCategoriesIds = categoriesIds.subtract(categoriesSet)
            throw NotFoundException("해당하는 사장님 가게 카테고리(${notExistsCategoriesIds})는 존재하지 않습니다", ErrorCode.NOT_FOUND_CATEGORY)
        }
    }

    fun validateExistsCategory(bossStoreCategoryRepository: BossStoreCategoryRepository, categoriesId: String) {
        if (!bossStoreCategoryRepository.existsById(categoriesId)) {
            throw NotFoundException("해당하는 사장님 가게 카테고리(${categoriesId})는 존재하지 않습니다", ErrorCode.NOT_FOUND_CATEGORY)
        }
    }

}
