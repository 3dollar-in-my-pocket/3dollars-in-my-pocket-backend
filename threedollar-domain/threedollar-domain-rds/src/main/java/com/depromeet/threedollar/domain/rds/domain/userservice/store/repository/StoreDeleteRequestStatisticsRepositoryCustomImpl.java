package com.depromeet.threedollar.domain.rds.domain.userservice.store.repository;

import static com.depromeet.threedollar.domain.rds.domain.userservice.store.QStoreDeleteRequest.storeDeleteRequest;

import java.time.LocalDate;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StoreDeleteRequestStatisticsRepositoryCustomImpl implements StoreDeleteRequestStatisticsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countBetweenDate(LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(storeDeleteRequest.id)
            .from(storeDeleteRequest)
            .where(
                storeDeleteRequest.createdAt.goe(startDate.atStartOfDay()),
                storeDeleteRequest.createdAt.lt(endDate.atStartOfDay().plusDays(1))
            )
            .fetchCount();
    }

}
