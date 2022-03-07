package com.depromeet.threedollar.domain.mongo.boss.domain.account

import com.depromeet.threedollar.domain.mongo.common.domain.BaseDocument
import com.depromeet.threedollar.domain.mongo.common.domain.BusinessNumber
import org.springframework.data.mongodb.core.mapping.Document

@Document("boss_account_v1")
class BossAccount(
    var name: String,
    val socialInfo: BossAccountSocialInfo,
    val businessNumber: BusinessNumber,
    var pushSettingsStatus: PushSettingsStatus
) : BaseDocument() {

    fun update(name: String, pushSettingsStatus: PushSettingsStatus) {
        this.name = name
        this.pushSettingsStatus = pushSettingsStatus
    }

    companion object {
        fun of(
            name: String,
            socialId: String,
            socialType: BossAccountSocialType,
            businessNumber: BusinessNumber,
            pushSettingsStatus: PushSettingsStatus = PushSettingsStatus.OFF
        ): BossAccount {
            return BossAccount(
                name = name,
                socialInfo = BossAccountSocialInfo.of(socialId, socialType),
                businessNumber = businessNumber,
                pushSettingsStatus = pushSettingsStatus
            )
        }
    }

}
