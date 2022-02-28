package com.depromeet.threedollar.domain.user.domain.advertisement.repository;

import com.depromeet.threedollar.domain.user.domain.advertisement.Advertisement;
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPlatformType;
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPositionType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.depromeet.threedollar.domain.user.domain.advertisement.QAdvertisement.advertisement;

@RequiredArgsConstructor
public class AdvertisementRepositoryCustomImpl implements AdvertisementRepositoryCustom {

    private final JPAQueryFactory queryFactory;

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
    public List<Advertisement> findAllWithPage(long size, int page) {
        return queryFactory.selectFrom(advertisement)
            .orderBy(advertisement.id.desc())
            .offset(page * size)
            .limit(size)
            .fetch();
    }

    // TODO 총 개수 캐싱
    @Override
    public long findAllCounts() {
        return queryFactory.selectFrom(advertisement)
            .fetchCount();
    }

}
