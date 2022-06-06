package com.depromeet.threedollar.domain.mongo.domain.bossservice.store

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.repository.BossDeletedStoreStatisticsRepositoryCustom

interface BossDeletedStoreRepository : MongoRepository<BossDeletedStore, String>, BossDeletedStoreStatisticsRepositoryCustom