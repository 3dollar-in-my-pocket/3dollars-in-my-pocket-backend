package com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement;

import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.repository.AdvertisementRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>, AdvertisementRepositoryCustom {

}
