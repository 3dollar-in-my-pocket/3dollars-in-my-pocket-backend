package com.depromeet.threedollar.domain.mongo.boss.domain.category.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategory

interface BossStoreCategoryRepositoryCustom {

    fun findAllCategoriesByIds(categoriesIds: Set<String>): List<BossStoreCategory>

}
