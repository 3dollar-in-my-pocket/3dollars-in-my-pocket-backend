package com.depromeet.threedollar.domain.mongo.domain.bossservice.account

import com.depromeet.threedollar.common.model.BusinessNumber
import com.depromeet.threedollar.domain.mongo.TestFixture

@TestFixture
object BossAccountFixture {

    fun create(
        socialId: String = "social-id",
        socialType: BossAccountSocialType = BossAccountSocialType.GOOGLE,
        name: String = "사장님 성함",
        businessNumber: BusinessNumber = BusinessNumber.of("000-00-00000"),
    ): BossAccount {
        return BossAccount(
            name = name,
            socialInfo = BossAccountSocialInfo(
                socialId,
                socialType
            ),
            businessNumber = businessNumber,
        )
    }

}
