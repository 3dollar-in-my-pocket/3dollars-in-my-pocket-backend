package com.depromeet.threedollar.api.core

import com.depromeet.threedollar.domain.rds.ThreeDollarDomainRdsRoot
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@ComponentScan(basePackageClasses = [
    ThreeDollarApiCoreRoot::class,
    ThreeDollarDomainRdsRoot::class
])
@Configuration
class ThreeDollarApiCoreRoot
