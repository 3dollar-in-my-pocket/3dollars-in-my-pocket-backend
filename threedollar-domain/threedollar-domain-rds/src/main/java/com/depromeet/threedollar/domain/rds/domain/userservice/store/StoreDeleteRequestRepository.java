package com.depromeet.threedollar.domain.rds.domain.userservice.store;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.StoreDeleteRequestRepositoryCustom;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.statistics.StoreDeleteRequestStatisticsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreDeleteRequestRepository extends JpaRepository<StoreDeleteRequest, Long>, StoreDeleteRequestRepositoryCustom, StoreDeleteRequestStatisticsRepositoryCustom {

}
