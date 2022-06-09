package com.depromeet.threedollar.domain.mongo.domain.bossservice.store

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.repository.BossStoreRepositoryCustom
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.repository.statistics.BossStoreStatisticsRepositoryCustom

interface BossStoreRepository : MongoRepository<BossStore, String>, BossStoreRepositoryCustom, BossStoreStatisticsRepositoryCustom
