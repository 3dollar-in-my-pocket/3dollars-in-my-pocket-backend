package com.depromeet.threedollar.api.boss.runner

import com.depromeet.threedollar.common.model.event.ApplicationStateChangedEvent
import com.depromeet.threedollar.common.type.ApplicationType
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextClosedEvent
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId

@Profile("dev", "prod")
@Component
class ApplicationEventRunner(
    private val eventPublisher: ApplicationEventPublisher
) : CommandLineRunner, ApplicationListener<ContextClosedEvent> {

    override fun run(vararg args: String) {
        eventPublisher.publishEvent(ApplicationStateChangedEvent.start(ApplicationType.BOSS_API, LocalDateTime.now(ZoneId.of("Asia/Seoul"))))
    }

    override fun onApplicationEvent(event: ContextClosedEvent) {
        eventPublisher.publishEvent(ApplicationStateChangedEvent.stop(ApplicationType.BOSS_API, LocalDateTime.now(ZoneId.of("Asia/Seoul"))))
    }

}
