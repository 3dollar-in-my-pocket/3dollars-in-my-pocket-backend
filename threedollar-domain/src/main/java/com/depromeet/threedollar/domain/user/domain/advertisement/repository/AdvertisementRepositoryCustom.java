package com.depromeet.threedollar.domain.user.domain.advertisement.repository;

import com.depromeet.threedollar.domain.user.domain.advertisement.Advertisement;
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPlatformType;
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPositionType;

import java.time.LocalDateTime;
import java.util.List;

public interface AdvertisementRepositoryCustom {

    List<Advertisement> findActivatedAdvertisementsByPositionAndPlatformAfterDate(AdvertisementPositionType positionType, AdvertisementPlatformType platformType, LocalDateTime dateTime);

    List<Advertisement> findAllWithPage(long size, int page);

    long findAllCounts();

}
