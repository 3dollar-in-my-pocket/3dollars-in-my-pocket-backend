package com.depromeet.threedollar.domain.mongo.domain.bossservice.account

import java.time.LocalDateTime
import org.springframework.data.mongodb.core.mapping.Document
import com.depromeet.threedollar.common.model.BusinessNumber
import com.depromeet.threedollar.domain.mongo.common.model.BaseDocument

@Document("boss_withdrawal_account_v1")
class BossWithdrawalAccount(
    val backupInfo: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BackupBossAccountInfo,
    val name: String,
    val socialInfo: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialInfo,
    val businessNumber: BusinessNumber,
    val isSetupNotification: Boolean,
) : BaseDocument() {

    companion object {
        fun newInstance(bossAccount: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount): com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossWithdrawalAccount {
            return com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossWithdrawalAccount(
                backupInfo = com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BackupBossAccountInfo.Companion.of(bossAccount),
                name = bossAccount.name,
                socialInfo = bossAccount.socialInfo.copy(),
                businessNumber = bossAccount.businessNumber.copy(),
                isSetupNotification = bossAccount.isSetupNotification
            )
        }
    }

}


data class BackupBossAccountInfo(
    val bossId: String,
    val bossCreatedAt: LocalDateTime,
) {

    companion object {
        fun of(bossAccount: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount): com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BackupBossAccountInfo {
            return com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BackupBossAccountInfo(
                bossId = bossAccount.id,
                bossCreatedAt = bossAccount.createdAt
            )
        }
    }

}
