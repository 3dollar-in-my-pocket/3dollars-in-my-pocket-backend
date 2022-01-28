package com.depromeet.threedollar.batch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan

@EnableBatchProcessing
@ConfigurationPropertiesScan(basePackages = ["com.depromeet.threedollar"])
@SpringBootApplication(scanBasePackages = ["com.depromeet.threedollar"])
internal class ThreeDollarsBatchApplication

fun main(args: Array<String>) {
    SpringApplication.run(ThreeDollarsBatchApplication::class.java, *args)
}
