package com.depromeet.threedollar.domain.rds.user.domain.storedelete;

import com.depromeet.threedollar.domain.rds.user.domain.storedelete.repository.StoreDeleteRequestRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreDeleteRequestRepository extends JpaRepository<StoreDeleteRequest, Long>, StoreDeleteRequestRepositoryCustom {

}
