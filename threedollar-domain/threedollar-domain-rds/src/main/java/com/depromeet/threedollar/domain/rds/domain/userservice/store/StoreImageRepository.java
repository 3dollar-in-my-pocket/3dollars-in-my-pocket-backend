package com.depromeet.threedollar.domain.rds.domain.userservice.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.StoreImageRepositoryCustom;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long>, StoreImageRepositoryCustom {

}
