package com.depromeet.threedollar.push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import static com.depromeet.threedollar.common.constants.PackageConstants.BASE_PACKAGE;

@ConfigurationPropertiesScan(basePackages = {BASE_PACKAGE})
@SpringBootApplication(scanBasePackages = {BASE_PACKAGE})
public class ThreeDollarsPushApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThreeDollarsPushApplication.class, args);
    }

}
