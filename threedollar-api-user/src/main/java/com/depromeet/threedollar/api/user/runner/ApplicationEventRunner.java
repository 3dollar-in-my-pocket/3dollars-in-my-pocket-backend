package com.depromeet.threedollar.api.user.runner;

import com.depromeet.threedollar.common.model.event.ApplicationStateChangedEvent;
import com.depromeet.threedollar.common.type.ApplicationType;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Profile({"dev", "prod"})
@RequiredArgsConstructor
@Component
public class ApplicationEventRunner implements CommandLineRunner, ApplicationListener<ContextClosedEvent> {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void run(String... args) {
        eventPublisher.publishEvent(ApplicationStateChangedEvent.start(ApplicationType.USER_API, LocalDateTime.now(ZoneId.of("Asia/Seoul"))));
    }

    @Override
    public void onApplicationEvent(@NotNull ContextClosedEvent event) {
        eventPublisher.publishEvent(ApplicationStateChangedEvent.stop(ApplicationType.USER_API, LocalDateTime.now(ZoneId.of("Asia/Seoul"))));
    }

}
