package com.depromeet.threedollar.domain.domain.medal.repository;

import com.depromeet.threedollar.domain.domain.medal.projection.MedalCountsStatisticsProjection;

import java.util.List;

public interface MedalStatisticsRepositoryCustom {

    List<MedalCountsStatisticsProjection> findUserMedalsCountsGroupByMedal();

}
