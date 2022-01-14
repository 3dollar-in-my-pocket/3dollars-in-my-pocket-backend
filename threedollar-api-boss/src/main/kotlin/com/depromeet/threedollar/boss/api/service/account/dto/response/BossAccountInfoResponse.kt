package com.depromeet.threedollar.boss.api.service.account.dto.response

import com.depromeet.threedollar.document.boss.document.account.BossAccount
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType

data class BossAccountInfoResponse(
    val bossId: String,
    val socialType: BossAccountSocialType,
    val name: String
) {

    companion object {
        fun of(bossAccount: BossAccount): BossAccountInfoResponse {
            return BossAccountInfoResponse(
                bossId = bossAccount.id,
                socialType = bossAccount.socialInfo.socialType,
                name = bossAccount.name
            )
        }
    }

}
