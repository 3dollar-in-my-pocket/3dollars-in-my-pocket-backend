package com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.repository;

import static com.depromeet.threedollar.domain.rds.common.support.QuerydslSupport.predicate;
import static com.depromeet.threedollar.domain.rds.user.domain.advertisement.QAdvertisement.advertisement;

import java.time.LocalDateTime;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.Advertisement;
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPlatformType;
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPositionType;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdvertisementRepositoryCustomImpl implements AdvertisementRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Nullable
    @Override
    public Advertisement findAdvertisementById(long advertisementId) {
        return queryFactory.selectFrom(advertisement)
            .where(
                advertisement.id.eq(advertisementId)
            ).fetchOne();
    }

    @Override
    public List<Advertisement> findActivatedAdvertisementsByPositionAndPlatformAfterDate(AdvertisementPositionType positionType, AdvertisementPlatformType platformType, LocalDateTime dateTime) {
        return queryFactory.selectFrom(advertisement)
            .where(
                advertisement.positionType.eq(positionType),
                advertisement.platformType.in(platformType, AdvertisementPlatformType.ALL),
                advertisement.dateTimeInterval.startDateTime.loe(dateTime),
                advertisement.dateTimeInterval.endDateTime.goe(dateTime)
            )
            .orderBy(advertisement.id.desc())
            .fetch();
    }

    @Override
    public List<Advertisement> findAllByPositionAndPlatformWithPaging(long size, int page, AdvertisementPlatformType platformType, AdvertisementPositionType positionType) {
        return queryFactory.selectFrom(advertisement)
            .where(
                predicate(platformType != null, () -> advertisement.platformType.eq(platformType)),
                predicate(positionType != null, () -> advertisement.positionType.eq(positionType))
            )
            .orderBy(advertisement.id.desc())
            .offset(page * size)
            .limit(size)
            .fetch();
    }

    @Override
    public long findAllCounts() {
        return queryFactory.selectFrom(advertisement)
            .fetchCount();
    }

}
