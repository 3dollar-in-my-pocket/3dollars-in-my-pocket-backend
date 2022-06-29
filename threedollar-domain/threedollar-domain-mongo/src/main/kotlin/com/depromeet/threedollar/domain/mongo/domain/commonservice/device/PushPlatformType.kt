package com.depromeet.threedollar.domain.mongo.domain.commonservice.device

import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.model.EnumModel
import com.depromeet.threedollar.common.type.OsPlatformType

enum class PushPlatformType(
    private val description: String,
    private val supportedOsPlatformTypes: List<OsPlatformType>,
) : EnumModel {

    FCM("FCM", listOf(OsPlatformType.IPHONE, OsPlatformType.ANDROID)),
    ;

    fun validateSupportedOsPlatformType(osPlatformType: OsPlatformType) {
        if (!this.supportedOsPlatformTypes.contains(osPlatformType)) {
            throw ForbiddenException("해당하는 OS($osPlatformType)에서 지원하지 않는 푸시 플랫폼(${this.name}) 입니다", ErrorCode.FORBIDDEN)
        }
    }

    override fun getDescription(): String {
        return description
    }

    override fun getKey(): String {
        return name
    }

}
