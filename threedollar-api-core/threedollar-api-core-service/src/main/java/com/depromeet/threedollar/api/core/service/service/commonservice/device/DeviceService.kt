package com.depromeet.threedollar.api.core.service.service.commonservice.device

import com.depromeet.threedollar.api.core.service.service.commonservice.device.dto.request.UpsertDeviceRequest
import com.depromeet.threedollar.common.model.UserMetaValue
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.Device
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeviceService(
    private val deviceRepository: DeviceRepository,
) {

    @Transactional
    fun upsertDevice(request: UpsertDeviceRequest) {
        val device: Device? = deviceRepository.findDeviceByAccountIdAndType(accountId = request.accountId, accountType = request.accountType)
        if (device == null) {
            deviceRepository.save(request.toDocument())
            return
        }

        if (hasSameDeviceInfo(device = device, request = request, userMetaValue = request.userMetaValue)) {
            return
        }

        device.updateDeviceInfo(
            pushToken = request.pushToken,
            pushPlatformType = request.pushPlatformType,
            osPlatformType = request.userMetaValue.osPlatform,
            appVersion = request.userMetaValue.appVersion
        )
        deviceRepository.save(device)
    }

    @Transactional
    fun deleteDevice(accountId: String, accountType: AccountType) {
        val device: Device? = deviceRepository.findDeviceByAccountIdAndType(accountId = accountId, accountType = accountType)
        device?.let {
            deviceRepository.delete(it)
        }
    }

    private fun hasSameDeviceInfo(device: Device, request: UpsertDeviceRequest, userMetaValue: UserMetaValue): Boolean {
        return device.hasSameDeviceInfo(
            pushPlatformType = request.pushPlatformType,
            osPlatformType = userMetaValue.osPlatform,
            appVersion = userMetaValue.appVersion,
            pushToken = request.pushToken
        )
    }

}

