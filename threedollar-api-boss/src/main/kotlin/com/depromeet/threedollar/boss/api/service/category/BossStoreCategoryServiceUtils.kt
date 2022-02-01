package com.depromeet.threedollar.boss.api.service.category

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository

object BossStoreCategoryServiceUtils {

    fun validateExistsCategories(bossStoreCategoryRepository: BossStoreCategoryRepository, categoriesIds: Set<String>) {
        val categories = bossStoreCategoryRepository.findCategoriesByIds(categoriesIds)
        if (categories.size != categoriesIds.size) {
            val notExistsCategoriesIds = categoriesIds.subtract(categories.map { it.id })
            throw NotFoundException("해당하는 id (${notExistsCategoriesIds})를 가진 카테고리는 존재하지 않습니다", ErrorCode.NOT_FOUND_CATEGORY_EXCEPTION)
        }
    }

}
