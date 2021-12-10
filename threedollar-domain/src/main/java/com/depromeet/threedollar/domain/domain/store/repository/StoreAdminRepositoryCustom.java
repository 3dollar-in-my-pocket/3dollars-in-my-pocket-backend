package com.depromeet.threedollar.domain.domain.store.repository;

import com.depromeet.threedollar.domain.domain.store.projection.StoreWithReportedCountProjection;

import java.util.List;

public interface StoreAdminRepositoryCustom {

    List<StoreWithReportedCountProjection> findStoresByMoreThanReportCntWithPagination(int cnt, long offset, int size);

}
