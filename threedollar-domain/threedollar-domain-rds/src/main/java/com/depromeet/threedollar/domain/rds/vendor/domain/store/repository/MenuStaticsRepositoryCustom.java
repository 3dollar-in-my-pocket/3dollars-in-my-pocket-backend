package com.depromeet.threedollar.domain.rds.vendor.domain.store.repository;

import java.util.List;

import com.depromeet.threedollar.domain.rds.vendor.domain.store.projection.MenuStatisticsProjection;

public interface MenuStaticsRepositoryCustom {

    List<MenuStatisticsProjection> countMenus();

}
