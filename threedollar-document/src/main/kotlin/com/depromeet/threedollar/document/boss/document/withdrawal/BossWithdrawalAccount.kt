package com.depromeet.threedollar.document.boss.document.withdrawal

import com.depromeet.threedollar.document.boss.document.account.BossAccount
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialInfo
import com.depromeet.threedollar.document.boss.document.account.PushSettingsStatus
import com.depromeet.threedollar.document.common.document.BaseDocument
import com.depromeet.threedollar.document.common.document.BusinessNumber
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
    val bossCreatedDateTime: LocalDateTime
) {

    companion object {
        fun of(bossAccount: BossAccount): BackupBossAccountInfo {
            return BackupBossAccountInfo(
                bossId = bossAccount.id,
                bossCreatedDateTime = bossAccount.createdDateTime
            )
        }
    }

}
