package com.depromeet.threedollar.domain.rds.config.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.depromeet.threedollar.domain.rds.ThreeDollarDomainRdsRoot;

@EntityScan(basePackageClasses = {ThreeDollarDomainRdsRoot.class})
@EnableJpaRepositories(basePackageClasses = {ThreeDollarDomainRdsRoot.class})
@EnableJpaAuditing
@Configuration
public class JpaConfig {

}
