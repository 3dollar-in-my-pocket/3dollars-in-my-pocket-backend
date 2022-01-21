package com.depromeet.threedollar.domain.user.domain.popup;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class PopupRepositoryTest {

    @Autowired
    private PopupRepository popupRepository;

    @AfterEach
    void cleanUp() {
        popupRepository.deleteAllInBatch();
    }

    @Test
    void 팝업_시작_날짜와_종료_날짜_사이인경우_활성화된_팝업에_포함된다_시작부근인경우() {
        // given
        PopupPositionType positionType = PopupPositionType.SPLASH;
        PopupPlatformType platformType = PopupPlatformType.AOS;
        String title = "광고 제목";
        String subTitle = "광고 내용\n광고 내용";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 11, 25, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 11, 26, 0, 0);
        String imageUrl = "https://image.url";
        String linkUrl = "https://link.com";
        String bgColor = "#ffffff";
        String fontColor = "#000000";

        popupRepository.save(PopupCreator.create(positionType, platformType, title, subTitle, imageUrl, linkUrl, bgColor, fontColor, startDateTime, endDateTime));

        // when
        List<Popup> findPopups = popupRepository.findActivatedPopupsByPositionAndPlatform(positionType, platformType, startDateTime.plusMinutes(1));

        // then
        assertAll(
            () -> assertThat(findPopups).hasSize(1),
            () -> assertThat(findPopups.get(0).getDetail().getTitle()).isEqualTo(title),
            () -> assertThat(findPopups.get(0).getDetail().getSubTitle()).isEqualTo(subTitle),
            () -> assertThat(findPopups.get(0).getDetail().getImageUrl()).isEqualTo(imageUrl),
            () -> assertThat(findPopups.get(0).getDetail().getLinkUrl()).isEqualTo(linkUrl),
            () -> assertThat(findPopups.get(0).getDetail().getBgColor()).isEqualTo(bgColor),
            () -> assertThat(findPopups.get(0).getDetail().getFontColor()).isEqualTo(fontColor),
            () -> assertThat(findPopups.get(0).getStartDateTime()).isEqualTo(startDateTime),
            () -> assertThat(findPopups.get(0).getEndDateTime()).isEqualTo(endDateTime),
            () -> assertThat(findPopups.get(0).getPlatformType()).isEqualTo(platformType)
        );
    }

    @Test
    void 팝업_시작_날짜와_종료_날짜_사이인경우_활성화된_팝업에_포함된다_종료부근인경우() {
        // given
        PopupPositionType positionType = PopupPositionType.SPLASH;
        PopupPlatformType platformType = PopupPlatformType.IOS;
        String title = "광고 제목";
        String subTitle = "광고 내용\n광고 내용";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 11, 24, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 11, 25, 0, 0);
        String imageUrl = "https://image.url";
        String linkUrl = "https://link.com";
        String bgColor = "#ffffff";
        String fontColor = "#000000";

        popupRepository.save(PopupCreator.create(positionType, platformType, title, subTitle, imageUrl, linkUrl, bgColor, fontColor, startDateTime, endDateTime));

        // when
        List<Popup> findPopups = popupRepository.findActivatedPopupsByPositionAndPlatform(positionType, platformType, endDateTime.minusMinutes(1));

        // then
        assertAll(
            () -> assertThat(findPopups).hasSize(1),
            () -> assertThat(findPopups.get(0).getDetail().getTitle()).isEqualTo(title),
            () -> assertThat(findPopups.get(0).getDetail().getSubTitle()).isEqualTo(subTitle),
            () -> assertThat(findPopups.get(0).getDetail().getImageUrl()).isEqualTo(imageUrl),
            () -> assertThat(findPopups.get(0).getDetail().getLinkUrl()).isEqualTo(linkUrl),
            () -> assertThat(findPopups.get(0).getDetail().getBgColor()).isEqualTo(bgColor),
            () -> assertThat(findPopups.get(0).getDetail().getFontColor()).isEqualTo(fontColor),
            () -> assertThat(findPopups.get(0).getStartDateTime()).isEqualTo(startDateTime),
            () -> assertThat(findPopups.get(0).getEndDateTime()).isEqualTo(endDateTime),
            () -> assertThat(findPopups.get(0).getPlatformType()).isEqualTo(platformType)
        );
    }

}
