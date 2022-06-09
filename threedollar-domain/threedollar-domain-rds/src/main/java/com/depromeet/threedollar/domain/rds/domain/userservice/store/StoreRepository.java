package com.depromeet.threedollar.domain.rds.domain.userservice.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.StoreRepositoryCustom;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.admin.StoreAdminRepositoryCustom;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.statistics.StoreStatisticsRepositoryCustom;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom, StoreStatisticsRepositoryCustom, StoreAdminRepositoryCustom {

}
