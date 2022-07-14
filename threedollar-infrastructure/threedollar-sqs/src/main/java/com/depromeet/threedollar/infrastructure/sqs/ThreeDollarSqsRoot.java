package com.depromeet.threedollar.infrastructure.sqs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackageClasses = {
        ThreeDollarSqsRoot.class
})
@Configuration
public class ThreeDollarSqsRoot {

}
