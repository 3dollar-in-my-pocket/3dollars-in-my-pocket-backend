package com.depromeet.threedollar.domain.mongo.boss.domain.account

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.repository.BossAccountRepositoryCustom
import com.depromeet.threedollar.domain.mongo.boss.domain.account.repository.BossAccountStatisticsRepositoryCustom

interface BossAccountRepository : MongoRepository<BossAccount, String>, BossAccountRepositoryCustom, BossAccountStatisticsRepositoryCustom
