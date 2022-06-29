package com.depromeet.threedollar.domain.mongo.domain.commonservice.device

import com.depromeet.threedollar.common.type.OsPlatformType
import com.depromeet.threedollar.domain.mongo.TestFixture

@TestFixture
object DeviceFixture {

    fun create(
        accountId: String,
        accountType: AccountType,
        pushPlatformType: PushPlatformType,
        osPlatformType: OsPlatformType,
        pushToken: String,
        appVersion: String?,
    ): Device {
        return Device(
            accountId = accountId,
            accountType = accountType,
            deviceInfo = DeviceInfo(
                pushPlatformType = pushPlatformType,
                osPlatformType = osPlatformType,
                appVersion = appVersion,
                pushToken = pushToken
            )
        )
    }

}
