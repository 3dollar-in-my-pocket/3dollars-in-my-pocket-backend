package com.depromeet.threedollar.admin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan(basePackages = ["com.depromeet.threedollar"])
@SpringBootApplication(scanBasePackages = ["com.depromeet.threedollar"])
class ThreeDollarAdminApplication

fun main(args: Array<String>) {
    runApplication<ThreeDollarAdminApplication>(*args)
}
