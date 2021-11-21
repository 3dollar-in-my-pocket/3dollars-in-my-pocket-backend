package com.depromeet.threedollar.domain.domain.menu.repository;

import com.depromeet.threedollar.domain.domain.menu.projection.MenuStatisticsProjection;

import java.util.List;

public interface MenuStaticsRepositoryCustom {

    List<MenuStatisticsProjection> countsGroupByMenu();

}
