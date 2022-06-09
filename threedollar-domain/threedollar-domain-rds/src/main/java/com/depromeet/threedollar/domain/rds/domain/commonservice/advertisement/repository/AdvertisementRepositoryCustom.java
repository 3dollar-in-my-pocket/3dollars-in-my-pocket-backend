package com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.Advertisement;
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPlatformType;
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPositionType;

public interface AdvertisementRepositoryCustom {

    @Nullable
    Advertisement findAdvertisementById(Long advertisementId);

    List<Advertisement> findActivatedAdvertisementsByPositionAndPlatformAfterDate(ApplicationType applicationType, AdvertisementPositionType positionType, AdvertisementPlatformType platformType, LocalDateTime dateTime);

    List<Advertisement> findAllByApplicationTypeAndPositionAndPlatformWithPaging(ApplicationType applicationType, long size, int page, @Nullable AdvertisementPlatformType platformType, @Nullable AdvertisementPositionType positionType);

    long findAllCountsByApplicationTypeAndPlatformTypeAndPositionType(ApplicationType applicationType, @Nullable AdvertisementPlatformType platformType, @Nullable AdvertisementPositionType positionType);

}
