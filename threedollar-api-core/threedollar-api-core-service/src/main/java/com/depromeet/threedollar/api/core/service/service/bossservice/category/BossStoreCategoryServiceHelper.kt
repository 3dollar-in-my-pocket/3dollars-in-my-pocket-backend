package com.depromeet.threedollar.api.core.service.service.bossservice.category

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository

object BossStoreCategoryServiceHelper {

    fun validateExistsCategories(bossStoreCategoryRepository: BossStoreCategoryRepository, categoriesIds: Set<String>) {
        val categories = bossStoreCategoryRepository.findAllCategoriesByIds(categoriesIds)
        if (categories.size != categoriesIds.size) {
            val categoriesSet = categories.asSequence()
                .map { category -> category.id }
                .toSet()
            val notExistsCategoriesIds = categoriesIds.subtract(categoriesSet)
            throw NotFoundException("해당하는 사장님 가게 카테고리(${notExistsCategoriesIds})는 존재하지 않습니다", ErrorCode.E404_NOT_EXISTS_CATEGORY)
        }
    }

    fun validateExistsCategory(bossStoreCategoryRepository: BossStoreCategoryRepository, categoryId: String) {
        if (!bossStoreCategoryRepository.existsById(categoryId)) {
            throw NotFoundException("해당하는 사장님 가게 카테고리(${categoryId})는 존재하지 않습니다", ErrorCode.E404_NOT_EXISTS_CATEGORY)
        }
    }

}
