package com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.admin;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.StoreWithReportedCountProjection;

import java.util.List;

public interface StoreAdminRepositoryCustom {

    List<StoreWithReportedCountProjection> findStoresByMoreThanReportCntWithPagination(int cnt, long offset, int size);

}
