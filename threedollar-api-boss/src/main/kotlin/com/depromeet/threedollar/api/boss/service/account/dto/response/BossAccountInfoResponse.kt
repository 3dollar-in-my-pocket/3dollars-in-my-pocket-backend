package com.depromeet.threedollar.api.boss.service.account.dto.response

import com.depromeet.threedollar.api.core.common.dto.BaseTimeResponse
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType

data class BossAccountInfoResponse(
    val bossId: String,
    val socialType: BossAccountSocialType,
    val name: String,
    val businessNumber: String,
    val isSetupNotification: Boolean
) : BaseTimeResponse() {

    companion object {
        fun of(bossAccount: BossAccount): BossAccountInfoResponse {
            val response = BossAccountInfoResponse(
                bossId = bossAccount.id,
                socialType = bossAccount.socialInfo.socialType,
                name = bossAccount.name,
                businessNumber = bossAccount.businessNumber.getNumberWithSeparator(),
                isSetupNotification = bossAccount.isSetupNotification
            )
            response.setBaseTime(bossAccount)
            return response
        }
    }

}
