package com.depromeet.threedollar.batch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import com.depromeet.threedollar.common.constants.PackageConstants.BASE_PACKAGE

@EnableBatchProcessing
@ConfigurationPropertiesScan(basePackages = [BASE_PACKAGE])
@SpringBootApplication(scanBasePackages = [BASE_PACKAGE])
internal class ThreeDollarsBatchApplication

fun main(args: Array<String>) {
    SpringApplication.run(ThreeDollarsBatchApplication::class.java, *args)
}
