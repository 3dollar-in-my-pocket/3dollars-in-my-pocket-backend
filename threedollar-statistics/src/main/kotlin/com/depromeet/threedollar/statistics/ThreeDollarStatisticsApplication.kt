package com.depromeet.threedollar.statistics

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan(basePackages = ["com.depromeet.threedollar.statistics"])
@SpringBootApplication(scanBasePackages = ["com.depromeet.threedollar.statistics"])
class ThreeDollarConsumerApplication

fun main(args: Array<String>) {
    runApplication<ThreeDollarConsumerApplication>(*args)
}
