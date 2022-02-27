package com.depromeet.threedollar.document.boss.document.withdrawal

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
) : BaseDocument()


data class BackupBossAccountInfo(
    val bossId: String,
    val bossCreatedDateTime: LocalDateTime
)
