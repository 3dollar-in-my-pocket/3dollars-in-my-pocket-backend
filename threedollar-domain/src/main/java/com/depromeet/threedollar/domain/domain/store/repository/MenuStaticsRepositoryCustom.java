package com.depromeet.threedollar.domain.domain.store.repository;

import com.depromeet.threedollar.domain.domain.store.projection.MenuStatisticsProjection;

import java.util.List;

public interface MenuStaticsRepositoryCustom {

    List<MenuStatisticsProjection> countsGroupByMenu();

}
