package com.depromeet.threedollar.domain.rds;

import static com.depromeet.threedollar.common.constants.PackageConstants.BASE_PACKAGE;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan(basePackages = BASE_PACKAGE)
@SpringBootApplication(scanBasePackages = BASE_PACKAGE)
class ThreeDollarDomainRdsRootTest extends ThreeDollarDomainRdsRoot {

    @Test
    void contextLoads() {

    }

}
