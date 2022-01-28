package com.depromeet.threedollar.document.boss.document.category

import com.depromeet.threedollar.document.boss.document.category.repository.BossStoreCategoryRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface BossStoreCategoryRepository : MongoRepository<BossStoreCategory, String>, BossStoreCategoryRepositoryCustom
