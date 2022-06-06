package com.depromeet.threedollar.domain.rds.domain.userservice.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.StoreDeleteRequestRepositoryCustom;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.StoreDeleteRequestStatisticsRepositoryCustom;

public interface StoreDeleteRequestRepository extends JpaRepository<StoreDeleteRequest, Long>, StoreDeleteRequestRepositoryCustom, StoreDeleteRequestStatisticsRepositoryCustom {

}
