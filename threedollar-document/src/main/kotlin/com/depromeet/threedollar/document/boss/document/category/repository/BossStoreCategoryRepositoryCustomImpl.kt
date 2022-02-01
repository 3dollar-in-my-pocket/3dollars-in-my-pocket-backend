package com.depromeet.threedollar.document.boss.document.category.repository

import com.depromeet.threedollar.document.boss.document.category.BossStoreCategory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.inValues

class BossStoreCategoryRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossStoreCategoryRepositoryCustom {

    override fun findCategoriesByIds(categoriesIds: Set<String>): List<BossStoreCategory> {
        return mongoTemplate.find(Query()
            .addCriteria(BossStoreCategory::id inValues categoriesIds)
        )
    }

}
