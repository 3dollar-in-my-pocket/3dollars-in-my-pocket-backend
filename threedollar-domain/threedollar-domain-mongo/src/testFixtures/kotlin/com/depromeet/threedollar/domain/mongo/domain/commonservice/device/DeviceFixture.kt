package com.depromeet.threedollar.domain.mongo.domain.commonservice.device

import com.depromeet.threedollar.common.type.OsPlatformType
import com.depromeet.threedollar.domain.mongo.TestFixture

@TestFixture
object DeviceFixture {

    @JvmOverloads
    @JvmStatic
    fun create(
        accountId: String,
        accountType: AccountType,
        pushToken: String = "device-push-token",
        pushPlatformType: PushPlatformType = PushPlatformType.FCM,
        osPlatformType: OsPlatformType = OsPlatformType.IPHONE,
        appVersion: String? = "1.0.0",
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
