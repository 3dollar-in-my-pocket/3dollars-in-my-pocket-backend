package com.depromeet.threedollar.common.model

import com.depromeet.threedollar.common.type.OsPlatformType

data class UserMetaValue(
    val osPlatform: OsPlatformType,
    val userAgent: String? = null,
    val clientIp: String? = null,
    val appVersion: String? = null
)
