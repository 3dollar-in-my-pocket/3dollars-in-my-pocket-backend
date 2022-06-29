package com.depromeet.threedollar.domain.rds.config.jpa;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class DatabaseBeanNameConstants {

    static final String PRIMARY_DATASOURCE = "primaryDataSource";
    static final String SECONDARY_DATASOURCE = "secondaryDataSource";
    static final String ROUTING_DATASOURCE = "routingDataSource";
    static final String TRANSACTION_MANAGER = "dbTransactionManager";

}
