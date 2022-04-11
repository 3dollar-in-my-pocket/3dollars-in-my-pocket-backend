package com.depromeet.threedollar.external;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan(basePackages = "com.depromeet.threedollar")
@SpringBootApplication(scanBasePackages = "com.depromeet.threedollar")
public class ThreeDollarExternalRootTest extends ThreeDollarExternalRoot {

    @Test
    void contextsLoad() {

    }

}
