package com.depromeet.threedollar.api.bossservice.controller.device.dto.request

import com.depromeet.threedollar.api.core.service.commonservice.device.dto.request.UpsertDeviceRequest
import com.depromeet.threedollar.common.utils.UserMetaSessionUtils
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.PushPlatformType
import javax.validation.constraints.NotBlank

private val ACCOUNT_TYPE = AccountType.BOSS_ACCOUNT

data class UpsertBossDeviceRequest(
    @field:NotBlank(message = "{device.pushToken.notBlank}")
    val pushToken: String = "",
    val pushPlatformType: PushPlatformType,
) {

    fun toUpsertDeviceRequest(accountId: String): UpsertDeviceRequest {
        return UpsertDeviceRequest(
            accountId = accountId,
            accountType = ACCOUNT_TYPE,
            pushToken = pushToken,
            pushPlatformType = pushPlatformType,
            userMetaValue = UserMetaSessionUtils.get()
        )
    }

}
