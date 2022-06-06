package com.depromeet.threedollar.domain.mongo.domain.bossservice.category.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategory

interface BossStoreCategoryRepositoryCustom {

    fun findAllCategoriesByIds(categoriesIds: Set<String>): List<BossStoreCategory>

    fun findCategoryById(categoryId: String): BossStoreCategory?

}
