package com.depromeet.threedollar.domain.mongo.foodtruck.domain.category

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.category.repository.BossStoreCategoryRepositoryCustom

interface BossStoreCategoryRepository : MongoRepository<BossStoreCategory, String>, BossStoreCategoryRepositoryCustom
