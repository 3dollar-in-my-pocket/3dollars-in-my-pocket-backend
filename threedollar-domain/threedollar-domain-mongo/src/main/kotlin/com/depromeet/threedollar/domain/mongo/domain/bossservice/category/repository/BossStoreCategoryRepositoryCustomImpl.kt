package com.depromeet.threedollar.domain.mongo.domain.bossservice.category.repository

import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.inValues
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository

@Repository
class BossStoreCategoryRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate,
) : BossStoreCategoryRepositoryCustom {

    override fun findAllCategoriesByIds(categoriesIds: Set<String>): List<BossStoreCategory> {
        return mongoTemplate.find(Query()
            .addCriteria(BossStoreCategory::id inValues categoriesIds)
        )
    }

    override fun findCategoryById(categoryId: String): BossStoreCategory? {
        return mongoTemplate.findOne(Query()
            .addCriteria(BossStoreCategory::id isEqualTo categoryId),
            BossStoreCategory::class.java
        )
    }

}
