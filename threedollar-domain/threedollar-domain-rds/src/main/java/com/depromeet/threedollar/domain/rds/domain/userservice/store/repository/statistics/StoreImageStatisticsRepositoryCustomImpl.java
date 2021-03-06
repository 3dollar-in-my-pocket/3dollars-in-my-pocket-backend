package com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.statistics;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static com.depromeet.threedollar.domain.rds.domain.userservice.store.QStoreImage.storeImage;

@RequiredArgsConstructor
public class StoreImageStatisticsRepositoryCustomImpl implements StoreImageStatisticsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countBetweenDate(LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(storeImage.id)
            .from(storeImage)
            .where(
                storeImage.createdAt.goe(startDate.atStartOfDay()),
                storeImage.createdAt.lt(endDate.atStartOfDay().plusDays(1))
            )
            .fetchCount();
    }

}
