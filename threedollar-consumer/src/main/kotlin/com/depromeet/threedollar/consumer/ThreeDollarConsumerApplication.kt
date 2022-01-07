package com.depromeet.threedollar.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan(basePackages = ["com.depromeet.threedollar"])
@SpringBootApplication(scanBasePackages = ["com.depromeet.threedollar"])
class ThreeDollarConsumerApplication

fun main(args: Array<String>) {
    runApplication<ThreeDollarConsumerApplication>(*args)
}
