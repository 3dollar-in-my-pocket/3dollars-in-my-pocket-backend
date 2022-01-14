package com.depromeet.threedollar.document.boss.document.store

import org.springframework.data.mongodb.repository.MongoRepository

interface BossStoreRepository : MongoRepository<BossStore, String>
