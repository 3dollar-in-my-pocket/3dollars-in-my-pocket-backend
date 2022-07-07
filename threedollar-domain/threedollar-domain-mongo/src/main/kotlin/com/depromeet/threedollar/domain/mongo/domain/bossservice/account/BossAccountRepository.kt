package com.depromeet.threedollar.domain.mongo.domain.bossservice.account

import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.repository.BossAccountRepositoryCustom
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.repository.statistics.BossAccountStatisticsRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface BossAccountRepository : MongoRepository<BossAccount, String>, BossAccountRepositoryCustom, BossAccountStatisticsRepositoryCustom
