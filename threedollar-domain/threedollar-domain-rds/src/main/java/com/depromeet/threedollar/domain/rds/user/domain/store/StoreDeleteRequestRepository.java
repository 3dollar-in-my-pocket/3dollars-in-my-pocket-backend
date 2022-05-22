package com.depromeet.threedollar.domain.rds.user.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.user.domain.store.repository.StoreDeleteRequestRepositoryCustom;
import com.depromeet.threedollar.domain.rds.user.domain.store.repository.StoreDeleteRequestStatisticsRepositoryCustom;

public interface StoreDeleteRequestRepository extends JpaRepository<StoreDeleteRequest, Long>, StoreDeleteRequestRepositoryCustom, StoreDeleteRequestStatisticsRepositoryCustom {

}
