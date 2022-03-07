package com.depromeet.threedollar.domain.mongo.boss.domain.store

import com.depromeet.threedollar.domain.mongo.boss.domain.store.repository.BossStoreRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface BossStoreRepository : MongoRepository<BossStore, String>, BossStoreRepositoryCustom
