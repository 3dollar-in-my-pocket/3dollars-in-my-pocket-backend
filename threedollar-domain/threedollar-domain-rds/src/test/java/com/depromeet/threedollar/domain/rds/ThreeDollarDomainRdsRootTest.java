package com.depromeet.threedollar.domain.rds;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan(basePackages = "com.depromeet.threedollar")
@SpringBootApplication(scanBasePackages = "com.depromeet.threedollar")
class ThreeDollarDomainRdsRootTest extends ThreeDollarDomainRdsRoot {

    @Test
    void contextLoads() {

    }

}
