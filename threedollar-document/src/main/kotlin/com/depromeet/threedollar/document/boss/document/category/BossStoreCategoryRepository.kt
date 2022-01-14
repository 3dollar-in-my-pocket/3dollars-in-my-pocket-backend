package com.depromeet.threedollar.document.boss.document.category

import org.springframework.data.mongodb.repository.MongoRepository

interface BossStoreCategoryRepository : MongoRepository<BossStoreCategory, String>
