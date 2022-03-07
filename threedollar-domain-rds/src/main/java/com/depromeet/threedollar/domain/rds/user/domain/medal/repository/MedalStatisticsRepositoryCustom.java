package com.depromeet.threedollar.domain.rds.user.domain.medal.repository;

import com.depromeet.threedollar.domain.rds.user.domain.medal.projection.MedalCountsStatisticsProjection;

import java.util.List;

public interface MedalStatisticsRepositoryCustom {

    List<MedalCountsStatisticsProjection> findUserMedalsCountsGroupByMedal();

    List<MedalCountsStatisticsProjection> findActiveCountsGroupByMedal();

}
