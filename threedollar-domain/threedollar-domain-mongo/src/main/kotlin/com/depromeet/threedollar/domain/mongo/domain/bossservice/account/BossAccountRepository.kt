package com.depromeet.threedollar.domain.mongo.domain.bossservice.account

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.repository.BossAccountRepositoryCustom
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.repository.statistics.BossAccountStatisticsRepositoryCustom

interface BossAccountRepository : MongoRepository<BossAccount, String>, BossAccountRepositoryCustom, BossAccountStatisticsRepositoryCustom
