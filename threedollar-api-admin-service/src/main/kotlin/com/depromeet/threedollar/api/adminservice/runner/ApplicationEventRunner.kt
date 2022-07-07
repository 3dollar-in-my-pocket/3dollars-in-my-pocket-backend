package com.depromeet.threedollar.api.adminservice.runner

import com.depromeet.threedollar.common.model.event.ApplicationStateChangedEvent
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.common.utils.LocalDateTimeUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextClosedEvent
import org.springframework.stereotype.Component

private val APPLICATION_TYPE = ApplicationType.ADMIN_API

@Profile("prod")
@Component
class ApplicationEventRunner(
    private val eventPublisher: ApplicationEventPublisher,

    @Value("\${threedollars.application.uid}")
    private val applicationUid: String,
) : CommandLineRunner, ApplicationListener<ContextClosedEvent> {

    override fun run(vararg args: String) {
        eventPublisher.publishEvent(ApplicationStateChangedEvent.start(APPLICATION_TYPE, LocalDateTimeUtils.now(), applicationUid))
    }

    override fun onApplicationEvent(event: ContextClosedEvent) {
        eventPublisher.publishEvent(ApplicationStateChangedEvent.stop(APPLICATION_TYPE, LocalDateTimeUtils.now(), applicationUid))
    }

}
