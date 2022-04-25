package com.depromeet.threedollar.domain.rds.user.domain.store;

import com.depromeet.threedollar.domain.rds.user.domain.store.repository.StoreImageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long>, StoreImageRepositoryCustom {

}
