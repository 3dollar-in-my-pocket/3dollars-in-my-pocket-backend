package com.depromeet.threedollar.domain.domain.storedelete.repository;

import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequest;

import java.util.List;

public interface StoreDeleteRequestRepositoryCustom {

	StoreDeleteRequest findStoreDeleteRequestByStoreIdAndUserId(Long storeId, Long userId);

	List<StoreDeleteRequest> findAllByStoreId(Long storeId);


}
