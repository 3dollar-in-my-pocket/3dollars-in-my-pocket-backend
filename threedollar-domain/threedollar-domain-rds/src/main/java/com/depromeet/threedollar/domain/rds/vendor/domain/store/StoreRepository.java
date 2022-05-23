package com.depromeet.threedollar.domain.rds.vendor.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.vendor.domain.store.repository.StoreAdminRepositoryCustom;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.repository.StoreRepositoryCustom;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.repository.StoreStatisticsRepositoryCustom;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom, StoreStatisticsRepositoryCustom, StoreAdminRepositoryCustom {

}
