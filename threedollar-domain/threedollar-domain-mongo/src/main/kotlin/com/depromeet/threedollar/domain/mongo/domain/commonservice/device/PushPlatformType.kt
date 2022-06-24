package com.depromeet.threedollar.domain.mongo.domain.commonservice.device

import com.depromeet.threedollar.common.type.OsPlatformType

enum class PushPlatformType(
    val description: String,
    val osPlatformType: OsPlatformType,
) {

    APNS("APNS", OsPlatformType.IPHONE),

}
