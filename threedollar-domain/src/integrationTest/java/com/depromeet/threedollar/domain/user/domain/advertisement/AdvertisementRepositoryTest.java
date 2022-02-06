package com.depromeet.threedollar.domain.user.domain.advertisement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AdvertisementRepositoryTest {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @AfterEach
    void cleanUp() {
        advertisementRepository.deleteAllInBatch();
    }

    @Test
    void 광고_시작_날짜와_종료_날짜_사이인경우_활성화된_광고에_포함된다_시작부근인경우() {
        // given
        AdvertisementPositionType positionType = AdvertisementPositionType.SPLASH;
        AdvertisementPlatformType platformType = AdvertisementPlatformType.AOS;
        String title = "광고 제목";
        String subTitle = "광고 내용\n광고 내용";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 11, 25, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 11, 26, 0, 0);
        String imageUrl = "https://image.url";
        String linkUrl = "https://link.com";
        String bgColor = "#ffffff";
        String fontColor = "#000000";

        advertisementRepository.save(AdvertisementCreator.create(positionType, platformType, title, subTitle, imageUrl, linkUrl, bgColor, fontColor, startDateTime, endDateTime));

        // when
        List<Advertisement> findAdvertisements = advertisementRepository.findActivatedAdvertisementsByPositionAndPlatformAfterDate(positionType, platformType, startDateTime.plusMinutes(1));

        // then
        assertAdvertisement(platformType, title, subTitle, startDateTime, endDateTime, imageUrl, linkUrl, bgColor, fontColor, findAdvertisements);
    }

    @Test
    void 광고_시작_날짜와_종료_날짜_사이인경우_활성화된_광고에_포함된다_종료부근인경우() {
        // given
        AdvertisementPositionType positionType = AdvertisementPositionType.SPLASH;
        AdvertisementPlatformType platformType = AdvertisementPlatformType.IOS;
        String title = "광고 제목";
        String subTitle = "광고 내용\n광고 내용";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 11, 24, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 11, 25, 0, 0);
        String imageUrl = "https://image.url";
        String linkUrl = "https://link.com";
        String bgColor = "#ffffff";
        String fontColor = "#000000";

        advertisementRepository.save(AdvertisementCreator.create(positionType, platformType, title, subTitle, imageUrl, linkUrl, bgColor, fontColor, startDateTime, endDateTime));

        // when
        List<Advertisement> findAdvertisements = advertisementRepository.findActivatedAdvertisementsByPositionAndPlatformAfterDate(positionType, platformType, endDateTime.minusMinutes(1));

        // then
        assertAdvertisement(platformType, title, subTitle, startDateTime, endDateTime, imageUrl, linkUrl, bgColor, fontColor, findAdvertisements);
    }

    private void assertAdvertisement(AdvertisementPlatformType platformType, String title, String subTitle, LocalDateTime startDateTime, LocalDateTime endDateTime, String imageUrl, String linkUrl, String bgColor, String fontColor, List<Advertisement> findAdvertisements) {
        assertAll(
            () -> assertThat(findAdvertisements).hasSize(1),
            () -> assertThat(findAdvertisements.get(0).getDetail().getTitle()).isEqualTo(title),
            () -> assertThat(findAdvertisements.get(0).getDetail().getSubTitle()).isEqualTo(subTitle),
            () -> assertThat(findAdvertisements.get(0).getDetail().getImageUrl()).isEqualTo(imageUrl),
            () -> assertThat(findAdvertisements.get(0).getDetail().getLinkUrl()).isEqualTo(linkUrl),
            () -> assertThat(findAdvertisements.get(0).getDetail().getBgColor()).isEqualTo(bgColor),
            () -> assertThat(findAdvertisements.get(0).getDetail().getFontColor()).isEqualTo(fontColor),
            () -> assertThat(findAdvertisements.get(0).getStartDateTime()).isEqualTo(startDateTime),
            () -> assertThat(findAdvertisements.get(0).getEndDateTime()).isEqualTo(endDateTime),
            () -> assertThat(findAdvertisements.get(0).getPlatformType()).isEqualTo(platformType)
        );
    }

}
