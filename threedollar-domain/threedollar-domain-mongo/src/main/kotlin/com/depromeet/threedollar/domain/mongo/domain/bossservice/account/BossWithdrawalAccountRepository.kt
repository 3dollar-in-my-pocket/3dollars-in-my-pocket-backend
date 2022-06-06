package com.depromeet.threedollar.domain.mongo.domain.bossservice.account

import org.springframework.data.mongodb.repository.MongoRepository

interface BossWithdrawalAccountRepository : MongoRepository<com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossWithdrawalAccount, String>
