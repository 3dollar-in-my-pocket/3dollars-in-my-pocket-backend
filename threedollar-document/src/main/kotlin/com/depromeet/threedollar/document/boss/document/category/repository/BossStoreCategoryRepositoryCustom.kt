package com.depromeet.threedollar.document.boss.document.category.repository

import com.depromeet.threedollar.document.boss.document.category.BossStoreCategory

interface BossStoreCategoryRepositoryCustom {

    fun findCategoriesByIds(categoriesIds: Set<String>): List<BossStoreCategory>

}
