package com.depromeet.threedollar.api.core.service.boss.category

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository

object BossStoreCategoryServiceUtils {

    fun validateExistsCategories(bossStoreCategoryRepository: BossStoreCategoryRepository, categoriesIds: Set<String>) {
        val categories = bossStoreCategoryRepository.findCategoriesByIds(categoriesIds)
        if (categories.size != categoriesIds.size) {
            val notExistsCategoriesIds = categoriesIds.subtract(categories.asSequence().map { it.id }.toSet())
            throw NotFoundException("해당하는 id (${notExistsCategoriesIds})를 가진 카테고리는 존재하지 않습니다", ErrorCode.NOTFOUND_CATEGORY)
        }
    }

}
