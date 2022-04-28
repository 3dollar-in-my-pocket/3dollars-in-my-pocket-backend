package com.depromeet.threedollar.domain.redis

import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan

@ConfigurationPropertiesScan(basePackages = ["com.depromeet.threedollar"])
@SpringBootApplication(scanBasePackages = ["com.depromeet.threedollar"])
internal class ThreeDollarDomainRedisRootTest {

    @Test
    fun contextsLoad() {

    }

}
