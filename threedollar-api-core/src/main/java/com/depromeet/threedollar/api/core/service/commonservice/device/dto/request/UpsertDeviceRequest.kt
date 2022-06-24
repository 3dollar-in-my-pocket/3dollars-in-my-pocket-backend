package com.depromeet.threedollar.api.core.service.commonservice.device.dto.request

import com.depromeet.threedollar.common.model.UserMetaValue
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.Device
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.PushPlatformType

data class UpsertDeviceRequest(
    val accountId: String,
    val accountType: AccountType,
    val pushToken: String,
    val pushPlatformType: PushPlatformType,
    val userMetaValue: UserMetaValue,
) {

    fun toDocument(): Device {
        return Device.of(
            accountId = accountId,
            accountType = accountType,
            pushPlatformType = pushPlatformType,
            osPlatformType = userMetaValue.osPlatform,
            pushToken = pushToken,
            appVersion = userMetaValue.appVersion,
        )
    }

}
