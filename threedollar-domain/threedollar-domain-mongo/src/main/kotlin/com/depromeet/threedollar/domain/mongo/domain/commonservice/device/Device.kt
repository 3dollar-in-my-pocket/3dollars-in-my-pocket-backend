package com.depromeet.threedollar.domain.mongo.domain.commonservice.device

import com.depromeet.threedollar.common.type.OsPlatformType
import com.depromeet.threedollar.domain.mongo.core.model.BaseDocument
import org.springframework.data.mongodb.core.mapping.Document

@Document("device_v1")
class Device(
    val accountId: String,
    val accountType: AccountType,
    var deviceInfo: DeviceInfo,
) : BaseDocument() {

    fun hasSameDeviceInfo(
        pushPlatformType: PushPlatformType,
        osPlatformType: OsPlatformType,
        appVersion: String?,
        pushToken: String,
    ): Boolean {
        return this.deviceInfo == DeviceInfo.of(
            pushPlatformType = pushPlatformType,
            osPlatformType = osPlatformType,
            appVersion = appVersion,
            pushToken = pushToken
        )
    }

    fun updateDeviceInfo(
        pushPlatformType: PushPlatformType,
        osPlatformType: OsPlatformType,
        appVersion: String?,
        pushToken: String,
    ) {
        this.deviceInfo = DeviceInfo.of(
            pushPlatformType = pushPlatformType,
            osPlatformType = osPlatformType,
            appVersion = appVersion,
            pushToken = pushToken
        )
    }

    companion object {
        fun of(
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
                deviceInfo = DeviceInfo.of(
                    pushPlatformType = pushPlatformType,
                    osPlatformType = osPlatformType,
                    appVersion = appVersion,
                    pushToken = pushToken
                )
            )
        }
    }

}


data class DeviceInfo(
    val pushPlatformType: PushPlatformType,
    val osPlatformType: OsPlatformType,
    val appVersion: String?,
    val pushToken: String,
) {

    init {
        this.pushPlatformType.validateSupportedOsPlatformType(this.osPlatformType)
    }

    companion object {
        fun of(
            pushPlatformType: PushPlatformType,
            osPlatformType: OsPlatformType,
            appVersion: String?,
            pushToken: String,
        ): DeviceInfo {
            return DeviceInfo(
                pushPlatformType = pushPlatformType,
                osPlatformType = osPlatformType,
                appVersion = appVersion,
                pushToken = pushToken
            )
        }
    }

}
