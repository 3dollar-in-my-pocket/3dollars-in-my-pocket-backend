package com.depromeet.threedollar.domain.rds.user.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.user.domain.store.repository.StoreAdminRepositoryCustom;
import com.depromeet.threedollar.domain.rds.user.domain.store.repository.StoreRepositoryCustom;
import com.depromeet.threedollar.domain.rds.user.domain.store.repository.StoreStatisticsRepositoryCustom;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom, StoreStatisticsRepositoryCustom, StoreAdminRepositoryCustom {

}
