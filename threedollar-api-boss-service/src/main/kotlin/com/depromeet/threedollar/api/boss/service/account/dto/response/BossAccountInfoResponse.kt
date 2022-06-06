package com.depromeet.threedollar.api.boss.service.account.dto.response

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse

data class BossAccountInfoResponse(
    val bossId: String,
    val socialType: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType,
    val name: String,
    val businessNumber: String,
    val isSetupNotification: Boolean,
) : AuditingTimeResponse() {

    companion object {
        fun of(bossAccount: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount): BossAccountInfoResponse {
            val response = BossAccountInfoResponse(
                bossId = bossAccount.id,
                socialType = bossAccount.socialInfo.socialType,
                name = bossAccount.name,
                businessNumber = bossAccount.businessNumber.getNumberWithSeparator(),
                isSetupNotification = bossAccount.isSetupNotification
            )
            response.setAuditingTimeByDocument(bossAccount)
            return response
        }
    }

}
