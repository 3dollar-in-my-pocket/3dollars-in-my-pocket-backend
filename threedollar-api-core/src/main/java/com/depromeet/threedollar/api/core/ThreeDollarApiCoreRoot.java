package com.depromeet.threedollar.api.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.depromeet.threedollar.domain.rds.ThreeDollarDomainRdsRoot;

@ComponentScan(basePackageClasses = {
    ThreeDollarApiCoreRoot.class,
    ThreeDollarDomainRdsRoot.class,
})
@Configuration
public class ThreeDollarApiCoreRoot {

}
