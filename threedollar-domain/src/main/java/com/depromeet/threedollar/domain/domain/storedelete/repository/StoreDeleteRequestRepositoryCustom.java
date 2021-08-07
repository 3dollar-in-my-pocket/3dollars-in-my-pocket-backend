package com.depromeet.threedollar.domain.domain.storedelete.repository;

import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequest;
import com.depromeet.threedollar.domain.domain.storedelete.projection.StoreDeleteRequestWithCountProjection;

import java.util.List;

public interface StoreDeleteRequestRepositoryCustom {

    List<StoreDeleteRequest> findAllByStoreId(Long storeId);

    List<StoreDeleteRequestWithCountProjection> findStoreHasDeleteRequestMoreThanCnt(int minCount);

}
