package com.depromeet.threedollar.domain.domain.storedelete.repository;

import java.util.List;

public interface StoreDeleteRequestRepositoryCustom {

    List<Long> findAllUserIdByStoreId(Long storeId);

}
