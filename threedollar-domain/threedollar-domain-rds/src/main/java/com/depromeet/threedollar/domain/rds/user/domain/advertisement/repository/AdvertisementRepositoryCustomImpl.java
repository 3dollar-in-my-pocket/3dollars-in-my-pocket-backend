package com.depromeet.threedollar.domain.rds.user.domain.advertisement.repository;

import static com.depromeet.threedollar.domain.rds.user.domain.advertisement.QAdvertisement.advertisement;

import java.time.LocalDateTime;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.user.domain.advertisement.Advertisement;
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPlatformType;
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPositionType;
import com.querydsl.core.types.dsl.BooleanExpression;
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
                eqPlatform(platformType),
                eqPosition(positionType)
            )
            .orderBy(advertisement.id.desc())
            .offset(page * size)
            .limit(size)
            .fetch();
    }

    private BooleanExpression eqPosition(AdvertisementPositionType positionType) {
        if (positionType == null) {
            return null;
        }
        return advertisement.positionType.eq(positionType);
    }

    private BooleanExpression eqPlatform(AdvertisementPlatformType platformType) {
        if (platformType == null) {
            return null;
        }
        return advertisement.platformType.eq(platformType);
    }

    @Override
    public long findAllCounts() {
        return queryFactory.selectFrom(advertisement)
            .fetchCount();
    }

}
