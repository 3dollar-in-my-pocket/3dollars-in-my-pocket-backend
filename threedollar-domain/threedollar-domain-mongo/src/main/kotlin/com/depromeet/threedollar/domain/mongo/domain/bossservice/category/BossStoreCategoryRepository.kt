package com.depromeet.threedollar.domain.mongo.domain.bossservice.category

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.repository.BossStoreCategoryRepositoryCustom

interface BossStoreCategoryRepository : MongoRepository<BossStoreCategory, String>, BossStoreCategoryRepositoryCustom
