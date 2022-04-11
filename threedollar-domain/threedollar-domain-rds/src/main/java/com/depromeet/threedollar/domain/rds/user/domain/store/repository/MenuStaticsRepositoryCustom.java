package com.depromeet.threedollar.domain.rds.user.domain.store.repository;

import java.util.List;

import com.depromeet.threedollar.domain.rds.user.domain.store.projection.MenuStatisticsProjection;

public interface MenuStaticsRepositoryCustom {

    List<MenuStatisticsProjection> countMenus();

}
