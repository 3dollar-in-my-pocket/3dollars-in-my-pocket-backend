package com.depromeet.threedollar.api.userservice.controller.device.dto.request

import com.depromeet.threedollar.api.core.service.service.commonservice.device.dto.request.UpsertDeviceRequest
import com.depromeet.threedollar.common.utils.UserMetaSessionUtils
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.PushPlatformType
import javax.validation.constraints.NotBlank

private val ACCOUNT_TYPE = AccountType.USER_ACCOUNT

data class UpsertUserDeviceRequest(
    @field:NotBlank(message = "{device.pushToken.notBlank}")
    val pushToken: String,
    val pushPlatformType: PushPlatformType,
) {

    fun toUpsertDeviceRequest(accountId: Long): UpsertDeviceRequest {
        return UpsertDeviceRequest(
            accountId = accountId.toString(),
            accountType = ACCOUNT_TYPE,
            pushToken = pushToken,
            pushPlatformType = pushPlatformType,
            userMetaValue = UserMetaSessionUtils.get()
        )
    }

}
