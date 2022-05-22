package com.depromeet.threedollar.domain.rds.user.domain.advertisement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.user.domain.advertisement.Advertisement;
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPlatformType;
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPositionType;

public interface AdvertisementRepositoryCustom {

    @Nullable
    Advertisement findAdvertisementById(long advertisementId);

    List<Advertisement> findActivatedAdvertisementsByPositionAndPlatformAfterDate(AdvertisementPositionType positionType, AdvertisementPlatformType platformType, LocalDateTime dateTime);

    List<Advertisement> findAllByPositionAndPlatformWithPaging(long size, int page, AdvertisementPlatformType platformType, AdvertisementPositionType positionType);

    long findAllCounts();

}
