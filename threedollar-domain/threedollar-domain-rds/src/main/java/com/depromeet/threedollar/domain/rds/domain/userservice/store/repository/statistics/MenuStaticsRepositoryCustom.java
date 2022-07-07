package com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.statistics;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.MenuStatisticsProjection;

import java.util.List;

public interface MenuStaticsRepositoryCustom {

    List<MenuStatisticsProjection> countMenus();

}
