package com.depromeet.threedollar.domain.mongo.boss.domain.account

data class BossAccountSocialInfo(
    val socialId: String,
    val socialType: BossAccountSocialType
) {

    companion object {
        fun of(socialId: String, socialType: BossAccountSocialType): BossAccountSocialInfo {
            return BossAccountSocialInfo(
                    socialId = socialId,
                    socialType = socialType
            )
        }
    }

}
