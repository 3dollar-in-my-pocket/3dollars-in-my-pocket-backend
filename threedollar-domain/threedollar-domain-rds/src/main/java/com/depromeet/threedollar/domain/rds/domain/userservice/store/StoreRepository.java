package com.depromeet.threedollar.domain.rds.domain.userservice.store;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.StoreRepositoryCustom;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.admin.StoreAdminRepositoryCustom;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.statistics.StoreStatisticsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom, StoreStatisticsRepositoryCustom, StoreAdminRepositoryCustom {

}
