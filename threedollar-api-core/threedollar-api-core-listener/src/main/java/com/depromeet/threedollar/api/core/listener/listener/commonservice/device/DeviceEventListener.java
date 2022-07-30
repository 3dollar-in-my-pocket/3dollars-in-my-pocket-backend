package com.depromeet.threedollar.api.core.listener.listener.commonservice.device;

import com.depromeet.threedollar.api.core.service.service.commonservice.device.DeviceService;
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType;
import com.depromeet.threedollar.domain.mongo.event.bossservice.account.BossLogOutedEvent;
import com.depromeet.threedollar.domain.mongo.event.bossservice.account.BossSignOutEvent;
import com.depromeet.threedollar.domain.rds.event.userservice.user.UserLogOutedEvent;
import com.depromeet.threedollar.domain.rds.event.userservice.user.UserSignOutedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeviceEventListener {

    private final DeviceService deviceService;

    @EventListener
    public void deleteUserDevice(UserSignOutedEvent event) {
        deviceService.deleteDevice(String.valueOf(event.getUserId()), AccountType.USER_ACCOUNT);
    }

    @EventListener
    public void deleteUserDevice(UserLogOutedEvent event) {
        deviceService.deleteDevice(String.valueOf(event.getUserId()), AccountType.USER_ACCOUNT);
    }

    @EventListener
    public void deleteBossDevice(BossSignOutEvent event) {
        deviceService.deleteDevice(event.getBossId(), AccountType.BOSS_ACCOUNT);
    }

    @EventListener
    public void deleteBossDevice(BossLogOutedEvent event) {
        deviceService.deleteDevice(event.getBossId(), AccountType.BOSS_ACCOUNT);
    }

}
