package com.depromeet.threedollar.document.boss.document.account

import com.depromeet.threedollar.document.common.document.BaseDocument
import com.depromeet.threedollar.document.common.document.BusinessNumber
import org.springframework.data.mongodb.core.mapping.Document

@Document("boss_account_v1")
class BossAccount(
    var name: String,
    val socialInfo: BossAccountSocialInfo,
    val businessNumber: BusinessNumber,
    var pushSettingsStatus: PushSettingsStatus = PushSettingsStatus.OFF,
) : BaseDocument() {

    fun update(name: String, pushSettingsStatus: PushSettingsStatus) {
        this.name = name
        this.pushSettingsStatus = pushSettingsStatus
    }

}
