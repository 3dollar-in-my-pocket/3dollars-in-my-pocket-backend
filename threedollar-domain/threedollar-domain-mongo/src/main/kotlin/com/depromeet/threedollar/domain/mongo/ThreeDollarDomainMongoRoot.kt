package com.depromeet.threedollar.domain.mongo

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = [ThreeDollarDomainMongoRoot::class])
@Configuration
class ThreeDollarDomainMongoRoot
