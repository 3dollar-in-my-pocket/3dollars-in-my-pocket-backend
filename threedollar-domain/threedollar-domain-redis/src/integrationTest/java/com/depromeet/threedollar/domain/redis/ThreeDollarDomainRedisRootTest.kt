package com.depromeet.threedollar.domain.redis

import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import com.depromeet.threedollar.common.constants.PackageConstants.BASE_PACKAGE

@ConfigurationPropertiesScan(basePackages = [BASE_PACKAGE])
@SpringBootApplication(scanBasePackages = [BASE_PACKAGE])
internal class ThreeDollarDomainRedisRootTest {

    @Test
    fun contextsLoad() {

    }

}
