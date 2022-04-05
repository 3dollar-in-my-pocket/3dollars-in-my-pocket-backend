package com.depromeet.threedollar.domain.rds.config.jpa;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

class RoutingDataSource extends AbstractRoutingDataSource {

    static final String PRIMARY = "PRIMARY";
    static final String SECONDARY = "SECONDARY";

    @Override
    protected Object determineCurrentLookupKey() {
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly() && TransactionSynchronizationManager.isActualTransactionActive() ? SECONDARY : PRIMARY;
    }

}
