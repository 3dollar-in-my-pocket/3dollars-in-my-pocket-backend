package com.depromeet.threedollar.domain.mongo.boss.domain.account

import com.depromeet.threedollar.domain.mongo.common.domain.BaseDocument
import com.depromeet.threedollar.domain.mongo.common.domain.BusinessNumber
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("boss_withdrawal_account_v1")
class BossWithdrawalAccount(
    val backupInfo: BackupBossAccountInfo,
    val name: String,
    val socialInfo: BossAccountSocialInfo,
    val businessNumber: BusinessNumber,
    val pushSettingsStatus: PushSettingsStatus,
) : BaseDocument() {

    companion object {
        fun newInstance(bossAccount: BossAccount): BossWithdrawalAccount {
            return BossWithdrawalAccount(
                backupInfo = BackupBossAccountInfo.of(bossAccount),
                name = bossAccount.name,
                socialInfo = bossAccount.socialInfo.copy(),
                businessNumber = bossAccount.businessNumber.copy(),
                pushSettingsStatus = bossAccount.pushSettingsStatus
            )
        }
    }

}


data class BackupBossAccountInfo(
    val bossId: String,
    val bossCreatedAt: LocalDateTime
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
