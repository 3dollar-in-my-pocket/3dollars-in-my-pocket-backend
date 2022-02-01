package com.depromeet.threedollar.document.boss.document.account

import com.depromeet.threedollar.document.TestFixture
import com.depromeet.threedollar.document.common.document.BusinessNumber

@TestFixture
object BossAccountCreator {

    fun create(
        socialId: String,
        socialType: BossAccountSocialType,
        name: String = "사장님 성함",
        businessNumber: BusinessNumber = BusinessNumber.of("012-12-12345"),
        pushSettingsStatus: PushSettingsStatus = PushSettingsStatus.OFF
    ): BossAccount {
        return BossAccount(
            name = name,
            socialInfo = BossAccountSocialInfo(socialId, socialType),
            businessNumber = businessNumber,
            pushSettingsStatus = pushSettingsStatus
        )
    }

}
