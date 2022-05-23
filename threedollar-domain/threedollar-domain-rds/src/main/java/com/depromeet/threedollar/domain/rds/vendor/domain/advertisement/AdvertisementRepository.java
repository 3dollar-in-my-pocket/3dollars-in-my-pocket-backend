package com.depromeet.threedollar.domain.rds.vendor.domain.advertisement;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.vendor.domain.advertisement.repository.AdvertisementRepositoryCustom;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>, AdvertisementRepositoryCustom {

}
