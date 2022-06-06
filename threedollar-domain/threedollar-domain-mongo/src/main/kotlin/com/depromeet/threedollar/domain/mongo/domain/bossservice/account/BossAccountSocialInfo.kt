package com.depromeet.threedollar.domain.mongo.domain.bossservice.account

data class BossAccountSocialInfo(
    val socialId: String,
    val socialType: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType,
) {

    companion object {
        fun of(
            socialId: String,
            socialType: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType,
        ): com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialInfo {
            return com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialInfo(
                socialId = socialId,
                socialType = socialType
            )
        }
    }

}
