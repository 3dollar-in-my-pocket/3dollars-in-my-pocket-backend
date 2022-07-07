package com.depromeet.threedollar.api.userservice.runner;

import com.depromeet.threedollar.common.model.event.ApplicationStateChangedEvent;
import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.common.utils.LocalDateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Profile("prod")
@RequiredArgsConstructor
@Component
public class ApplicationEventRunner implements CommandLineRunner, ApplicationListener<ContextClosedEvent> {

    private static final ApplicationType APPLICATION_TYPE = ApplicationType.USER_API;

    private final ApplicationEventPublisher eventPublisher;

    @Value("${threedollars.application.uid}")
    private String applicationUid;

    @Override
    public void run(String... args) {
        eventPublisher.publishEvent(ApplicationStateChangedEvent.start(APPLICATION_TYPE, LocalDateTimeUtils.now(), applicationUid));
    }

    @Override
    public void onApplicationEvent(@NotNull ContextClosedEvent event) {
        eventPublisher.publishEvent(ApplicationStateChangedEvent.stop(APPLICATION_TYPE, LocalDateTimeUtils.now(), applicationUid));
    }

}
