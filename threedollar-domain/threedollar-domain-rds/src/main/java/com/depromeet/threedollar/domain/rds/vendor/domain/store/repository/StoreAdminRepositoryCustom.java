package com.depromeet.threedollar.domain.rds.vendor.domain.store.repository;

import java.util.List;

import com.depromeet.threedollar.domain.rds.vendor.domain.store.projection.StoreWithReportedCountProjection;

public interface StoreAdminRepositoryCustom {

    List<StoreWithReportedCountProjection> findStoresByMoreThanReportCntWithPagination(int cnt, long offset, int size);

}
