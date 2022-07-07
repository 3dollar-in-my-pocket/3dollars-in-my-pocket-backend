package com.depromeet.threedollar.domain.mongo.domain.bossservice.store

import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.repository.statistics.BossDeletedStoreStatisticsRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface BossDeletedStoreRepository : MongoRepository<BossDeletedStore, String>, BossDeletedStoreStatisticsRepositoryCustom
