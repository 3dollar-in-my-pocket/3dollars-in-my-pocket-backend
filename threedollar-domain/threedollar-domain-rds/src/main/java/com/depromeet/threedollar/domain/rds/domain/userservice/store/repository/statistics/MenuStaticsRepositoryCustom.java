package com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.statistics;

import java.util.List;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.MenuStatisticsProjection;

public interface MenuStaticsRepositoryCustom {

    List<MenuStatisticsProjection> countMenus();

}
