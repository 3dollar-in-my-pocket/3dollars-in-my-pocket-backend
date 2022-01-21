package com.depromeet.threedollar.document.boss.document.category.repository

import com.depromeet.threedollar.document.boss.document.category.BossStoreCategory

interface BossStoreCategoryRepositoryCustom {

    fun findCategoriesByIds(categoriesIds: List<String>): List<BossStoreCategory>

}
