package com.depromeet.threedollar.domain.rds.user.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.user.domain.store.repository.StoreImageRepositoryCustom;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long>, StoreImageRepositoryCustom {

}
