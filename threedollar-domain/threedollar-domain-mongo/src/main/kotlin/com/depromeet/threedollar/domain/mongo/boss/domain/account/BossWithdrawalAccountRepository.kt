package com.depromeet.threedollar.domain.mongo.boss.domain.account

import org.springframework.data.mongodb.repository.MongoRepository

interface BossWithdrawalAccountRepository : MongoRepository<BossWithdrawalAccount, String>
