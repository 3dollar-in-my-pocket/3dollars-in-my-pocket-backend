package com.depromeet.threedollar.domain.rds.config.jpa;

import static com.depromeet.threedollar.common.type.ReplicationType.SECONDARY;
import static java.text.Collator.PRIMARY;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly() && TransactionSynchronizationManager.isActualTransactionActive() ? SECONDARY : PRIMARY;
    }

}
