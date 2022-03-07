package com.depromeet.threedollar.domain.mongo.boss.domain.account

import com.depromeet.threedollar.domain.mongo.TestFixture
import com.depromeet.threedollar.domain.mongo.common.domain.BusinessNumber

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
            socialInfo = BossAccountSocialInfo.of(socialId, socialType),
            businessNumber = businessNumber,
            pushSettingsStatus = pushSettingsStatus
        )
    }

}
