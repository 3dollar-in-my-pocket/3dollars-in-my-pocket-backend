package com.depromeet.threedollar.domain.rds.user.domain.store.repository;

import com.depromeet.threedollar.domain.rds.user.domain.store.projection.MenuStatisticsProjection;

import java.util.List;

public interface MenuStaticsRepositoryCustom {

    List<MenuStatisticsProjection> countsGroupByMenu();

}
