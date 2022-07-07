package com.depromeet.threedollar.domain.mongo.domain.bossservice.category

import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.repository.BossStoreCategoryRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface BossStoreCategoryRepository : MongoRepository<BossStoreCategory, String>, BossStoreCategoryRepositoryCustom
