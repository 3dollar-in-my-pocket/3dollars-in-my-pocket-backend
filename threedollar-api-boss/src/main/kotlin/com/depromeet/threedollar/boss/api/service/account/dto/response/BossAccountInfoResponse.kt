package com.depromeet.threedollar.boss.api.service.account.dto.response

import com.depromeet.threedollar.domain.boss.domain.account.BossAccount
import com.depromeet.threedollar.domain.boss.domain.account.BossAccountSocialType

data class BossAccountInfoResponse(
    val bossAccountId: Long,
    val socialType: BossAccountSocialType,
    val name: String
) {

    companion object {
        fun of(bossAccount: BossAccount): BossAccountInfoResponse {
            return BossAccountInfoResponse(
                bossAccountId = bossAccount.id,
                socialType = bossAccount.socialInfo.socialType,
                name = bossAccount.name
            )
        }
    }

}
