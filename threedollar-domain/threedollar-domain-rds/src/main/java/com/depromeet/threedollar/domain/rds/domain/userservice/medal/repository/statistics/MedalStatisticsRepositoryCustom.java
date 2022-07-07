package com.depromeet.threedollar.domain.rds.domain.userservice.medal.repository.statistics;

import com.depromeet.threedollar.domain.rds.domain.userservice.medal.projection.MedalCountsStatisticsProjection;

import java.util.List;

public interface MedalStatisticsRepositoryCustom {

    List<MedalCountsStatisticsProjection> countsUserMedalGroupByMedalType();

    List<MedalCountsStatisticsProjection> countActiveMedalsGroupByMedalType();

}
