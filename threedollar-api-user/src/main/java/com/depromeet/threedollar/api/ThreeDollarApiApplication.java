package com.depromeet.threedollar.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan(basePackages = "com.depromeet.threedollar")
@SpringBootApplication(scanBasePackages = "com.depromeet.threedollar")
public class ThreeDollarApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThreeDollarApiApplication.class, args);
    }

}
