package com.depromeet.threedollar.domain.user.domain.storedelete.repository;

import java.util.List;

public interface StoreDeleteRequestRepositoryCustom {

    List<Long> findAllUserIdByStoreIdWithLock(Long storeId);

	long findCountsByUserId(Long userId);

}
