package com.depromeet.threedollar.domain.mongo.domain.bossservice.account

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.repository.BossAccountStatisticsRepositoryCustom

interface BossAccountRepository : MongoRepository<com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount, String>,
    com.depromeet.threedollar.domain.mongo.domain.bossservice.account.repository.BossAccountRepositoryCustom, BossAccountStatisticsRepositoryCustom
