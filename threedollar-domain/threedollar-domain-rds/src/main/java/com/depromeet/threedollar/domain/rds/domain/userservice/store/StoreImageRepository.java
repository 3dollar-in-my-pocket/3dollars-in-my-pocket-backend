package com.depromeet.threedollar.domain.rds.domain.userservice.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.StoreImageRepositoryCustom;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.statistics.StoreImageStatisticsRepositoryCustom;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long>, StoreImageRepositoryCustom, StoreImageStatisticsRepositoryCustom {

}
