package com.depromeet.threedollar.domain.mongo.domain.bossservice.store

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.repository.BossStoreRepositoryCustom
import com.depromeet.threedollar.domain.mongo.boss.domain.store.repository.BossStoreStatisticsRepositoryCustom

interface BossStoreRepository : MongoRepository<BossStore, String>, BossStoreRepositoryCustom, BossStoreStatisticsRepositoryCustom
