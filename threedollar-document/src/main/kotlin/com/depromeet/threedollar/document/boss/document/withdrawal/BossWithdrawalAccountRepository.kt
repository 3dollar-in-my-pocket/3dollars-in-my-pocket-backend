package com.depromeet.threedollar.document.boss.document.withdrawal

import org.springframework.data.mongodb.repository.MongoRepository

interface BossWithdrawalAccountRepository : MongoRepository<BossWithdrawalAccount, String> {
}
