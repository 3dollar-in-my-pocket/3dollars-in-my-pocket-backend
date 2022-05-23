package com.depromeet.threedollar.common.model

import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.common.type.OsPlatformType

data class UserMetaValue(
    val traceId: String?,
    val osPlatform: OsPlatformType,
    val userAgent: String?,
    val clientIp: String?,
    val applicationType: ApplicationType,
    val appVersion: String?,
) {

    companion object {
        fun of(
            osPlatform: OsPlatformType,
            userAgent: String? = null,
            clientIp: String? = null,
            applicationType: ApplicationType,
            appVersion: String? = null,
            traceId: String?,
        ): UserMetaValue {
            return UserMetaValue(
                osPlatform = osPlatform,
                userAgent = userAgent,
                clientIp = clientIp,
                applicationType = applicationType,
                appVersion = appVersion,
                traceId = traceId,
            )
        }
    }

}
