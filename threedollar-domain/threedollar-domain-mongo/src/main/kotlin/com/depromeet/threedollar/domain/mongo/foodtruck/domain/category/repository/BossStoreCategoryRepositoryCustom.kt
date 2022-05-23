package com.depromeet.threedollar.domain.mongo.foodtruck.domain.category.repository

import com.depromeet.threedollar.domain.mongo.foodtruck.domain.category.BossStoreCategory

interface BossStoreCategoryRepositoryCustom {

    fun findAllCategoriesByIds(categoriesIds: Set<String>): List<BossStoreCategory>

    fun findCategoryById(categoryId: String): BossStoreCategory?

}
