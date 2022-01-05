package com.depromeet.threedollar.api.config.resolver

import com.depromeet.threedollar.common.type.PlatformType

data class UserMetaInfo(
    val platformType: PlatformType,
    val userAgent: String,
    val sourceIp: String,
)
