package com.depromeet.threedollar.domain.rds.domain.userservice.medal.repository.statistics;

import com.depromeet.threedollar.domain.rds.core.support.OrderByNull;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalStatus;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.projection.MedalCountsStatisticsProjection;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.projection.QMedalCountsStatisticsProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.depromeet.threedollar.domain.rds.domain.userservice.medal.QMedal.medal;
import static com.depromeet.threedollar.domain.rds.domain.userservice.medal.QUserMedal.userMedal;

@RequiredArgsConstructor
public class MedalStatisticsRepositoryCustomImpl implements MedalStatisticsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MedalCountsStatisticsProjection> countsUserMedalGroupByMedalType() {
        return queryFactory.select(new QMedalCountsStatisticsProjection(medal.name, userMedal.id.count()))
            .from(medal)
            .innerJoin(userMedal).on(userMedal.medal.id.eq(medal.id))
            .groupBy(medal.id)
            .orderBy(OrderByNull.DEFAULT)
            .fetch();
    }

    @Override
    public List<MedalCountsStatisticsProjection> countActiveMedalsGroupByMedalType() {
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
