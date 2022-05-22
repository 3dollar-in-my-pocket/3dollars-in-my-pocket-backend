package com.depromeet.threedollar.domain.mongo.boss.domain.store

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.repository.BossDeletedStoreStatisticsRepositoryCustom

interface BossDeletedStoreRepository : MongoRepository<BossDeletedStore, String>, BossDeletedStoreStatisticsRepositoryCustom
