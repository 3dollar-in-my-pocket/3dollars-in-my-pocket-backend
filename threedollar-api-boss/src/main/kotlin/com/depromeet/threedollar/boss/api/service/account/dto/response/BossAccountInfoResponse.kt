package com.depromeet.threedollar.boss.api.service.account.dto.response

import com.depromeet.threedollar.boss.api.controller.dto.response.BaseTimeResponse
import com.depromeet.threedollar.document.boss.document.account.BossAccount
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.boss.document.account.PushSettingsStatus

data class BossAccountInfoResponse(
    val bossId: String,
    val socialType: BossAccountSocialType,
    val name: String,
    val businessNumber: String,
    val pushSettingsStatus: PushSettingsStatus
) : BaseTimeResponse() {

    companion object {
        fun of(bossAccount: BossAccount): BossAccountInfoResponse {
            val response = BossAccountInfoResponse(
                bossId = bossAccount.id,
                socialType = bossAccount.socialInfo.socialType,
                name = bossAccount.name,
                businessNumber = bossAccount.businessNumber.getNumberWithSeparator(),
                pushSettingsStatus = bossAccount.pushSettingsStatus
            )
            response.setBaseTime(bossAccount)
            return response
        }
    }

}
