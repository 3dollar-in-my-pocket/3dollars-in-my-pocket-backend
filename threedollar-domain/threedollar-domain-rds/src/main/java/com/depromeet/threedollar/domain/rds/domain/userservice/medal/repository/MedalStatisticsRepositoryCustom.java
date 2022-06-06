package com.depromeet.threedollar.domain.rds.domain.userservice.medal.repository;

import java.util.List;

import com.depromeet.threedollar.domain.rds.domain.userservice.medal.projection.MedalCountsStatisticsProjection;

public interface MedalStatisticsRepositoryCustom {

    List<MedalCountsStatisticsProjection> countsUserMedalGroupByMedalType();

    List<MedalCountsStatisticsProjection> countActiveMedalsGroupByMedalType();

}
