package com.depromeet.threedollar.domain.rds.user.domain.advertisement;

import com.depromeet.threedollar.domain.rds.user.domain.advertisement.repository.AdvertisementRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>, AdvertisementRepositoryCustom {

}
