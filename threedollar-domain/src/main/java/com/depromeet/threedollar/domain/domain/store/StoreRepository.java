package com.depromeet.threedollar.domain.domain.store;

import com.depromeet.threedollar.domain.domain.store.repository.StoreAdminRepositoryCustom;
import com.depromeet.threedollar.domain.domain.store.repository.StoreRepositoryCustom;
import com.depromeet.threedollar.domain.domain.store.repository.StoreStatisticsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom, StoreStatisticsRepositoryCustom, StoreAdminRepositoryCustom {

}
