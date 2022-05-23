package com.depromeet.threedollar.domain.rds.vendor.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.vendor.domain.store.repository.StoreDeleteRequestRepositoryCustom;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.repository.StoreDeleteRequestStatisticsRepositoryCustom;

public interface StoreDeleteRequestRepository extends JpaRepository<StoreDeleteRequest, Long>, StoreDeleteRequestRepositoryCustom, StoreDeleteRequestStatisticsRepositoryCustom {

}
