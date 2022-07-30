package com.depromeet.threedollar.api.core.runner;

import com.depromeet.threedollar.common.model.event.ApplicationStateChangedEvent;
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

    private final ApplicationEventPublisher eventPublisher;

    @Value("${threedollars.application.uid}")
    private String applicationUid;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void run(String... args) {
        eventPublisher.publishEvent(ApplicationStateChangedEvent.start(applicationName, LocalDateTimeUtils.now(), applicationUid));
    }

    @Override
    public void onApplicationEvent(@NotNull ContextClosedEvent event) {
        eventPublisher.publishEvent(ApplicationStateChangedEvent.stop(applicationName, LocalDateTimeUtils.now(), applicationUid));
    }

}
