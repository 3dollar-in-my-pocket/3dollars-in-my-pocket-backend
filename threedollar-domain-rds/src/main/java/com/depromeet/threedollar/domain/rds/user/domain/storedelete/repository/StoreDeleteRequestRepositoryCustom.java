package com.depromeet.threedollar.domain.rds.user.domain.storedelete.repository;

import java.util.List;

public interface StoreDeleteRequestRepositoryCustom {

    List<Long> findAllUserIdByStoreIdWithLock(Long storeId);

	long countsByUserId(Long userId);

}
