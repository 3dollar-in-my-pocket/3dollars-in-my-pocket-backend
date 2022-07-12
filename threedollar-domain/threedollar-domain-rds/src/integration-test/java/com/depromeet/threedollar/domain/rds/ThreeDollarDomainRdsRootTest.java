package com.depromeet.threedollar.domain.rds;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import static com.depromeet.threedollar.common.constants.PackageConstants.BASE_PACKAGE;

@ConfigurationPropertiesScan(basePackages = BASE_PACKAGE)
@SpringBootApplication(scanBasePackages = BASE_PACKAGE)
class ThreeDollarDomainRdsRootTest {

    @Test
    void contextLoads() {

    }

}
