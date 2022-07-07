package com.depromeet.threedollar.domain.mongo.domain.bossservice.store

import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.repository.BossStoreRepositoryCustom
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.repository.statistics.BossStoreStatisticsRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface BossStoreRepository : MongoRepository<BossStore, String>, BossStoreRepositoryCustom, BossStoreStatisticsRepositoryCustom
