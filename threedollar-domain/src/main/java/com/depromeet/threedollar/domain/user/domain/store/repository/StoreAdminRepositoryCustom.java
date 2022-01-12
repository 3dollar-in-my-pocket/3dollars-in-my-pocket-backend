package com.depromeet.threedollar.domain.user.domain.store.repository;

import com.depromeet.threedollar.domain.user.domain.store.projection.StoreWithReportedCountProjection;

import java.util.List;

public interface StoreAdminRepositoryCustom {

    List<StoreWithReportedCountProjection> findStoresByMoreThanReportCntWithPagination(int cnt, long offset, int size);

}
