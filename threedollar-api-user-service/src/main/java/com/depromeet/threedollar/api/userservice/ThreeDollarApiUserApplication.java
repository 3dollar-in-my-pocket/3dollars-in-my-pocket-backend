package com.depromeet.threedollar.api.userservice;

import static com.depromeet.threedollar.common.constants.PackageConstants.BASE_PACKAGE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan(basePackages = BASE_PACKAGE)
@SpringBootApplication(scanBasePackages = BASE_PACKAGE)
public class ThreeDollarApiUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThreeDollarApiUserApplication.class, args);
    }

}
