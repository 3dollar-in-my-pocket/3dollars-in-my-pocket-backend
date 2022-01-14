package com.depromeet.threedollar.document.boss.document.account

import com.depromeet.threedollar.document.boss.document.account.repository.BossAccountRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface BossAccountRepository : MongoRepository<BossAccount, String>, BossAccountRepositoryCustom
