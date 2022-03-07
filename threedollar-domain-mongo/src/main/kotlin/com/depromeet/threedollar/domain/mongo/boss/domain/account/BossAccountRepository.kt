package com.depromeet.threedollar.domain.mongo.boss.domain.account

import org.springframework.data.mongodb.repository.MongoRepository

interface BossAccountRepository : MongoRepository<BossAccount, String>, com.depromeet.threedollar.domain.mongo.boss.domain.account.repository.BossAccountRepositoryCustom
