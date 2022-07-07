package com.depromeet.threedollar.api.adminservice

import com.depromeet.threedollar.common.constants.PackageConstants.BASE_PACKAGE
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan(basePackages = [BASE_PACKAGE])
@SpringBootApplication(scanBasePackages = [BASE_PACKAGE])
class ThreeDollarAdminApplication

fun main(args: Array<String>) {
    runApplication<ThreeDollarAdminApplication>(*args)
}
