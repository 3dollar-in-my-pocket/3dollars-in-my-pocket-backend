package com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.repository.AdvertisementRepositoryCustom;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>, AdvertisementRepositoryCustom {

}
