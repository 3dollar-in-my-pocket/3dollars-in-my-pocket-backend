package com.depromeet.threedollar.domain.mongo.boss.domain.store

import org.springframework.data.mongodb.repository.MongoRepository

interface BossDeletedStoreRepository : MongoRepository<BossDeletedStore, String>
