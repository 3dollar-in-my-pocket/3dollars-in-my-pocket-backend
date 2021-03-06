package com.depromeet.threedollar.domain.rds.config.jpa;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static com.depromeet.threedollar.common.type.ReplicationType.PRIMARY;
import static com.depromeet.threedollar.common.type.ReplicationType.SECONDARY;
import static com.depromeet.threedollar.domain.rds.config.jpa.DatabaseBeanNameConstants.PRIMARY_DATASOURCE;
import static com.depromeet.threedollar.domain.rds.config.jpa.DatabaseBeanNameConstants.ROUTING_DATASOURCE;
import static com.depromeet.threedollar.domain.rds.config.jpa.DatabaseBeanNameConstants.SECONDARY_DATASOURCE;

@Configuration
public class DataSourceConfig {

    @Primary
    @Bean
    public DataSource dataSource(DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean(ROUTING_DATASOURCE)
    public DataSource routingDataSource(
        @Qualifier(PRIMARY_DATASOURCE) DataSource primaryDataSource,
        @Qualifier(SECONDARY_DATASOURCE) DataSource secondaryDataSource
    ) {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(PRIMARY, primaryDataSource);
        dataSourceMap.put(SECONDARY, secondaryDataSource);

        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(primaryDataSource);

        return routingDataSource;
    }

    @Bean(PRIMARY_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.primary.hikari")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean(SECONDARY_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.secondary.hikari")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .build();
    }

}
