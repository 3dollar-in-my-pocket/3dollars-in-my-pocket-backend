package com.depromeet.threedollar.domain.mongo.domain.bossservice.account

import com.depromeet.threedollar.common.model.BusinessNumber
import com.depromeet.threedollar.domain.mongo.core.model.BaseDocument
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("boss_withdrawal_account_v1")
class BossWithdrawalAccount(
    val backupInfo: BackupBossAccountInfo,
    val name: String,
    val socialInfo: BossAccountSocialInfo,
    val businessNumber: BusinessNumber,
    val isSetupNotification: Boolean,
) : BaseDocument() {

    companion object {
        fun newInstance(bossAccount: BossAccount): BossWithdrawalAccount {
            return BossWithdrawalAccount(
                backupInfo = BackupBossAccountInfo.of(bossAccount),
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
        fun of(bossAccount: BossAccount): BackupBossAccountInfo {
            return BackupBossAccountInfo(
                bossId = bossAccount.id,
                bossCreatedAt = bossAccount.createdAt
            )
        }
    }

}
