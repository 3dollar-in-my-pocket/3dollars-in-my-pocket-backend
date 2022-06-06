package com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.Advertisement;
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPlatformType;
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPositionType;

public interface AdvertisementRepositoryCustom {

    @Nullable
    Advertisement findAdvertisementById(long advertisementId);

    List<Advertisement> findActivatedAdvertisementsByPositionAndPlatformAfterDate(AdvertisementPositionType positionType, AdvertisementPlatformType platformType, LocalDateTime dateTime);

    List<Advertisement> findAllByPositionAndPlatformWithPaging(long size, int page, AdvertisementPlatformType platformType, AdvertisementPositionType positionType);

    long findAllCounts();

}
