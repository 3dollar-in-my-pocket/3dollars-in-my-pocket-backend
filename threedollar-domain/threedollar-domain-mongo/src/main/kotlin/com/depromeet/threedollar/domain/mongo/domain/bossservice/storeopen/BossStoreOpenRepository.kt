package com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.repository.BossStoreOpenRepositoryCustom

@Repository
interface BossStoreOpenRepository : MongoRepository<BossStoreOpen, String>, BossStoreOpenRepositoryCustom
