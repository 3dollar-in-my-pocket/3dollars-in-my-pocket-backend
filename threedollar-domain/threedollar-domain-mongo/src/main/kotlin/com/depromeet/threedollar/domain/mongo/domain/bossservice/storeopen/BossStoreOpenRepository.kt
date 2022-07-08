package com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen

import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.repository.BossStoreOpenRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface BossStoreOpenRepository : MongoRepository<BossStoreOpen, String>, BossStoreOpenRepositoryCustom
