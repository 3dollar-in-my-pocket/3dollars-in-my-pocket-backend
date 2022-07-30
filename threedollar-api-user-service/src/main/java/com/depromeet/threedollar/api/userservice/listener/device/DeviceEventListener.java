package com.depromeet.threedollar.api.userservice.listener.device;

import com.depromeet.threedollar.api.core.service.commonservice.device.DeviceService;
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType;
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
    public void deleteDevice(UserSignOutedEvent event) {
        deviceService.deleteDevice(String.valueOf(event.getUserId()), AccountType.USER_ACCOUNT);
    }

    @EventListener
    public void deleteDevice(UserLogOutedEvent event) {
        deviceService.deleteDevice(String.valueOf(event.getUserId()), AccountType.USER_ACCOUNT);
    }

}
