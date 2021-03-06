package com.depromeet.threedollar.api.core.service.service.commonservice.device.dto.request

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

    companion object {
        @JvmStatic
        fun of(
            accountId: String,
            accountType: AccountType,
            pushToken: String,
            pushPlatformType: PushPlatformType,
            userMetaValue: UserMetaValue,
        ): UpsertDeviceRequest {
            return UpsertDeviceRequest(
                accountId = accountId,
                accountType = accountType,
                pushToken = pushToken,
                pushPlatformType = pushPlatformType,
                userMetaValue = userMetaValue
            )
        }
    }

}
