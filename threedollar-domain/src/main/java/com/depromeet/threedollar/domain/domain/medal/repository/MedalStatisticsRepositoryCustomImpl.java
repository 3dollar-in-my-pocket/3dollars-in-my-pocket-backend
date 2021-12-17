package com.depromeet.threedollar.domain.domain.medal.repository;

import com.depromeet.threedollar.domain.config.querydsl.OrderByNull;
import com.depromeet.threedollar.domain.domain.medal.UserMedalStatus;
import com.depromeet.threedollar.domain.domain.medal.projection.MedalCountsStatisticsProjection;
import com.depromeet.threedollar.domain.domain.medal.projection.QMedalCountsStatisticsProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.depromeet.threedollar.domain.domain.medal.QMedal.medal;
import static com.depromeet.threedollar.domain.domain.medal.QUserMedal.userMedal;

@RequiredArgsConstructor
public class MedalStatisticsRepositoryCustomImpl implements MedalStatisticsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MedalCountsStatisticsProjection> findUserMedalsCountsGroupByMedal() {
        return queryFactory.select(new QMedalCountsStatisticsProjection(medal.name, userMedal.id.count()))
            .from(medal)
            .innerJoin(userMedal).on(userMedal.medal.id.eq(medal.id))
            .groupBy(medal.id)
            .orderBy(OrderByNull.DEFAULT)
            .fetch();
    }

    @Override
    public List<MedalCountsStatisticsProjection> findActiveCountsGroupByMedal() {
        return queryFactory.select(new QMedalCountsStatisticsProjection(medal.name, userMedal.id.count()))
            .from(medal)
            .innerJoin(userMedal).on(userMedal.medal.id.eq(medal.id))
            .where(
                userMedal.status.eq(UserMedalStatus.ACTIVE)
            )
            .groupBy(medal.id)
            .orderBy(OrderByNull.DEFAULT)
            .fetch();
    }

}
