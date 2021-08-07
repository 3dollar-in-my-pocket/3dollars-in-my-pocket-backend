package com.depromeet.threedollar.admin

import com.depromeet.threedollar.application.ThreeDollarApplicationRoot
import com.depromeet.threedollar.domain.ThreeDollarDomainRoot
import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableAdminServer
@SpringBootApplication(scanBasePackageClasses = [
    ThreeDollarAdminApplication::class,
    ThreeDollarApplicationRoot::class,
    ThreeDollarDomainRoot::class
])
class ThreeDollarAdminApplication

fun main(args: Array<String>) {
    runApplication<ThreeDollarAdminApplication>(*args)
}
