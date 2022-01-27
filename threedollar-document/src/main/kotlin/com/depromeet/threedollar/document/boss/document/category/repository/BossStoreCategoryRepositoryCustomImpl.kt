package com.depromeet.threedollar.document.boss.document.category.repository

import com.depromeet.threedollar.document.boss.document.category.BossStoreCategory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query

class BossStoreCategoryRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
): BossStoreCategoryRepositoryCustom {

    override fun findCategoriesByIds(categoriesIds: List<String>): List<BossStoreCategory> {
        return mongoTemplate.find(
            query(
                where("_id").`in`(categoriesIds)
            ),
            BossStoreCategory::class.java
        )
    }

}
