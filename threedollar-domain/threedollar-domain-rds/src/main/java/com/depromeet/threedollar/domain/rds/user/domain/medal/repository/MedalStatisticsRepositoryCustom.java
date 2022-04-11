package com.depromeet.threedollar.domain.rds.user.domain.medal.repository;

import java.util.List;

import com.depromeet.threedollar.domain.rds.user.domain.medal.projection.MedalCountsStatisticsProjection;

public interface MedalStatisticsRepositoryCustom {

    List<MedalCountsStatisticsProjection> countsUserMedalGroupByMedalType();

    List<MedalCountsStatisticsProjection> countActiveMedalsGroupByMedalType();

}
