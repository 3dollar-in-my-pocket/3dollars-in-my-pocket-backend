package com.depromeet.threedollar.common.model

import com.depromeet.threedollar.common.type.PlatformType

data class UserMetaValue(
    val platformType: PlatformType,
    val userAgent: String,
    val sourceIp: String,
)
