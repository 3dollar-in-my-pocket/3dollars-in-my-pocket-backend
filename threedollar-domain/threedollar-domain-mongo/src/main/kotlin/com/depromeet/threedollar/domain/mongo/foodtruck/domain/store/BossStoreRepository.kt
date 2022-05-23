package com.depromeet.threedollar.domain.mongo.foodtruck.domain.store

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.store.repository.BossStoreRepositoryCustom
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.store.repository.BossStoreStatisticsRepositoryCustom

interface BossStoreRepository : MongoRepository<BossStore, String>, BossStoreRepositoryCustom, BossStoreStatisticsRepositoryCustom
