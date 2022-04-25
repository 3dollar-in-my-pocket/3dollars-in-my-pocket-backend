package com.depromeet.threedollar.domain.mongo.boss.domain.category

import com.depromeet.threedollar.domain.mongo.boss.domain.category.repository.BossStoreCategoryRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface BossStoreCategoryRepository : MongoRepository<BossStoreCategory, String>, BossStoreCategoryRepositoryCustom
