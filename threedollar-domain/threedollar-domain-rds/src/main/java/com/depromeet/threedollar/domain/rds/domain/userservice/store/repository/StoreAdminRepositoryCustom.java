package com.depromeet.threedollar.domain.rds.domain.userservice.store.repository;

import java.util.List;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.StoreWithReportedCountProjection;

public interface StoreAdminRepositoryCustom {

    List<StoreWithReportedCountProjection> findStoresByMoreThanReportCntWithPagination(int cnt, long offset, int size);

}
