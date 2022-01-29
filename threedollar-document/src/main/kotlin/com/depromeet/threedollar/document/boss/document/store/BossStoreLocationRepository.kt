package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.document.boss.document.store.repository.BossStoreLocationRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface BossStoreLocationRepository : MongoRepository<BossStoreLocation, String>, BossStoreLocationRepositoryCustom
