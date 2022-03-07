package com.depromeet.threedollar.domain.mongo.boss.domain.store

import com.depromeet.threedollar.domain.mongo.boss.domain.store.repository.BossStoreLocationRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface BossStoreLocationRepository : MongoRepository<BossStoreLocation, String>, BossStoreLocationRepositoryCustom
