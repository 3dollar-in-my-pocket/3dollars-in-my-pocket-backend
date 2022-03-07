package com.depromeet.threedollar.domain.rds.config.jpa;

import com.depromeet.threedollar.domain.rds.ThreeDollarDomainRdsRoot;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackageClasses = {ThreeDollarDomainRdsRoot.class})
@EnableJpaRepositories(basePackageClasses = {ThreeDollarDomainRdsRoot.class})
@EnableJpaAuditing
@Configuration
public class JpaConfig {

}
