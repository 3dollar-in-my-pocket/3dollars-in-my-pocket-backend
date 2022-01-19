package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.document.boss.document.store.repository.BossStoreRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface BossStoreRepository : MongoRepository<BossStore, String>, BossStoreRepositoryCustom
