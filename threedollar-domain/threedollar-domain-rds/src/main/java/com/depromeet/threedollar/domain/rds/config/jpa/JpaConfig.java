package com.depromeet.threedollar.domain.rds.config.jpa;

import static com.depromeet.threedollar.domain.rds.config.jpa.DatabaseBeanNameConstants.TRANSACTION_MANAGER;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.depromeet.threedollar.domain.rds.ThreeDollarDomainRdsRoot;

@EntityScan(basePackageClasses = {ThreeDollarDomainRdsRoot.class})
@EnableJpaRepositories(
    basePackageClasses = {ThreeDollarDomainRdsRoot.class},
    transactionManagerRef = TRANSACTION_MANAGER
)
@EnableJpaAuditing
@Configuration
public class JpaConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Primary
    @Bean(name = TRANSACTION_MANAGER)
    public PlatformTransactionManager dbTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager.getEntityManagerFactory());
        return transactionManager;
    }

}
