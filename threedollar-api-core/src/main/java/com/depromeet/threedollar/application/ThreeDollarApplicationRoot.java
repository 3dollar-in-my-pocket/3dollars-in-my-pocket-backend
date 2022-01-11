package com.depromeet.threedollar.application;

import com.depromeet.threedollar.domain.ThreeDollarDomainRoot;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackageClasses = {
    ThreeDollarApplicationRoot.class,
    ThreeDollarDomainRoot.class,
})
@Configuration
public class ThreeDollarApplicationRoot {

}
