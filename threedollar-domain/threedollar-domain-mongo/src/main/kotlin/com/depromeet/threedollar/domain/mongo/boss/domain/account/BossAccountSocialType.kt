package com.depromeet.threedollar.domain.mongo.boss.domain.account

import com.depromeet.threedollar.common.model.EnumModel

enum class BossAccountSocialType(
    private val description: String,
) : EnumModel {

    KAKAO("카카오"),
    APPLE("애플"),
    GOOGLE("구글"),
    NAVER("네이버"),
    ;

    override fun getKey(): String {
        return name
    }

    override fun getDescription(): String {
        return description
    }

}
