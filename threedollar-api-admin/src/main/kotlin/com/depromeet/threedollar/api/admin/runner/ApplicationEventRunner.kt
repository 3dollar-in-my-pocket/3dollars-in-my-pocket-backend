package com.depromeet.threedollar.api.admin.runner

import java.time.LocalDateTime
import java.time.ZoneId
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextClosedEvent
import org.springframework.stereotype.Component
import com.depromeet.threedollar.common.model.event.ApplicationStateChangedEvent
import com.depromeet.threedollar.common.type.ApplicationType

@Profile("dev", "prod")
@Component
class ApplicationEventRunner(
    private val eventPublisher: ApplicationEventPublisher,

    @Value("\${threedollars.aplication.uid}")
    private val applicationUid: String
) : CommandLineRunner, ApplicationListener<ContextClosedEvent> {

    override fun run(vararg args: String) {
        eventPublisher.publishEvent(ApplicationStateChangedEvent.start(ApplicationType.ADMIN_API, LocalDateTime.now(ZoneId.of("Asia/Seoul")), applicationUid))
    }

    override fun onApplicationEvent(event: ContextClosedEvent) {
        eventPublisher.publishEvent(ApplicationStateChangedEvent.stop(ApplicationType.ADMIN_API, LocalDateTime.now(ZoneId.of("Asia/Seoul")), applicationUid))
    }

}
