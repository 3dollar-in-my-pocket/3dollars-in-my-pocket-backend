package com.depromeet.threedollar.domain.rds.config.jpa;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static com.depromeet.threedollar.common.type.ReplicationType.SECONDARY;
import static java.text.Collator.PRIMARY;

class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly() && TransactionSynchronizationManager.isActualTransactionActive() ? SECONDARY : PRIMARY;
    }

}
