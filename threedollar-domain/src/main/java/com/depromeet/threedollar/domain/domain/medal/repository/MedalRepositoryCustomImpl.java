package com.depromeet.threedollar.domain.domain.medal.repository;

import com.depromeet.threedollar.domain.domain.medal.Medal;
import com.depromeet.threedollar.domain.domain.medal.MedalAcquisitionConditionType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.depromeet.threedollar.domain.domain.medal.QMedal.medal;
import static com.depromeet.threedollar.domain.domain.medal.QMedalAcquisitionCondition.medalAcquisitionCondition;

@RequiredArgsConstructor
public class MedalRepositoryCustomImpl implements MedalRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Medal> findAllActiveMedals() {
        return queryFactory.selectFrom(medal)
            .innerJoin(medal.acquisitionCondition, medalAcquisitionCondition)
            .fetch();
    }

    @Override
    public List<Medal> findAllByConditionType(MedalAcquisitionConditionType conditionType) {
        return queryFactory.selectFrom(medal)
            .innerJoin(medal.acquisitionCondition, medalAcquisitionCondition).fetchJoin()
            .where(
                medal.acquisitionCondition.conditionType.eq(conditionType)
            ).fetch();
    }

}
