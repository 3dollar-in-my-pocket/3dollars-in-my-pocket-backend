package com.depromeet.threedollar.foodtruck.domain.config;

import com.depromeet.threedollar.foodtruck.domain.BossDomainRoot;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackageClasses = {BossDomainRoot.class})
@EnableJpaRepositories(basePackageClasses = {BossDomainRoot.class})
@EnableJpaAuditing
@Configuration
public class JpaConfig {

}
