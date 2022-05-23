package com.depromeet.threedollar.domain.mongo.foodtruck.domain.account

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.account.repository.BossAccountRepositoryCustom
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.account.repository.BossAccountStatisticsRepositoryCustom

interface BossAccountRepository : MongoRepository<BossAccount, String>, BossAccountRepositoryCustom, BossAccountStatisticsRepositoryCustom
