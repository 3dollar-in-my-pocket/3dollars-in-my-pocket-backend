package com.depromeet.threedollar.domain.rds.domain.userservice.medal.repository;

import com.depromeet.threedollar.domain.rds.domain.userservice.medal.Medal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalAcquisitionConditionType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.depromeet.threedollar.domain.rds.domain.userservice.medal.QMedal.medal;
import static com.depromeet.threedollar.domain.rds.domain.userservice.medal.QMedalAcquisitionCondition.medalAcquisitionCondition;

@RequiredArgsConstructor
public class MedalRepositoryCustomImpl implements MedalRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Nullable
    @Override
    public Medal findMedalById(Long medalId) {
        return queryFactory.selectFrom(medal)
            .innerJoin(medal.acquisitionCondition, medalAcquisitionCondition).fetchJoin()
            .where(
                medal.id.eq(medalId)
            ).fetchOne();
    }

    @Override
    public List<Medal> findAllActiveMedals() {
        return queryFactory.selectFrom(medal)
            .innerJoin(medal.acquisitionCondition, medalAcquisitionCondition).fetchJoin()
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
