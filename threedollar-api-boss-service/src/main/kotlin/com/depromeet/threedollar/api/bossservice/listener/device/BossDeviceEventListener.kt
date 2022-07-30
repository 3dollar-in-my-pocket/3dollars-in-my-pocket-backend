package com.depromeet.threedollar.api.bossservice.listener.device

import com.depromeet.threedollar.api.core.service.commonservice.device.DeviceService
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.event.bossservice.registration.BossSignOutEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class BossDeviceEventListener(
    private val deviceService: DeviceService,
) {

    @EventListener
    fun deleteDeviceByBossId(event: BossSignOutEvent) {
        deviceService.deleteDevice(accountId = event.bossId, accountType = AccountType.BOSS_ACCOUNT)
    }

}
