package com.depromeet.threedollar.document.boss.document.account

import org.springframework.data.mongodb.repository.MongoRepository

interface BossWithdrawalAccountRepository : MongoRepository<BossWithdrawalAccount, String> {
}
