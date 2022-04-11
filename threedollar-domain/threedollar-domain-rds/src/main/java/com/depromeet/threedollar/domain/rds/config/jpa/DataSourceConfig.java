package com.depromeet.threedollar.domain.rds.config.jpa;

import static com.depromeet.threedollar.common.type.ReplicationType.PRIMARY;
import static com.depromeet.threedollar.common.type.ReplicationType.SECONDARY;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {

    private static final String PRIMARY_DATASOURCE = "primaryDataSource";
    private static final String SECONDARY_DATASOURCE = "secondaryDataSource";
    private static final String ROUTING_DATASOURCE = "routingDataSource";

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
