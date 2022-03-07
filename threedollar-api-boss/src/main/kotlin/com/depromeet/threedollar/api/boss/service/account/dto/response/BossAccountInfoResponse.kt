package com.depromeet.threedollar.api.boss.service.account.dto.response

import com.depromeet.threedollar.api.boss.controller.dto.response.BaseTimeResponse

data class BossAccountInfoResponse(
        val bossId: String,
        val socialType: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType,
        val name: String,
        val businessNumber: String,
        val pushSettingsStatus: com.depromeet.threedollar.domain.mongo.boss.domain.account.PushSettingsStatus
) : BaseTimeResponse() {

    companion object {
        fun of(bossAccount: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount): BossAccountInfoResponse {
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
