package com.depromeet.threedollar.domain.mongo.boss.domain.account

import com.depromeet.threedollar.domain.mongo.boss.domain.account.repository.BossAccountRepositoryCustom
import com.depromeet.threedollar.domain.mongo.boss.domain.account.repository.BossAccountStatisticsRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface BossAccountRepository : MongoRepository<BossAccount, String>, BossAccountRepositoryCustom, BossAccountStatisticsRepositoryCustom
