package com.depromeet.threedollar.common.model

import com.depromeet.threedollar.common.type.OsPlatformType

data class UserMetaValue(
    val osPlatform: OsPlatformType,
    val userAgent: String?,
    val sourceIp: String?,
    val appVersion: String?
)
