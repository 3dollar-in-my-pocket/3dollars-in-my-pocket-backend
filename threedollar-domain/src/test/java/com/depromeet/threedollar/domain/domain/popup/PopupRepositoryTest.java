package com.depromeet.threedollar.domain.domain.popup;

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
        popupRepository.deleteAll();
    }

    @Test
    void 날짜_범위안에_속한_팝업을_조회한다_시작_이후인경우() {
        // given
        PopupPlatformType platformType = PopupPlatformType.AOS;
        LocalDateTime startDateTime = LocalDateTime.of(2021, 11, 25, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 11, 26, 0, 0);
        String imageUrl = "https://image.url";
        String linkUrl = "https://link.com";

        popupRepository.save(PopupCreator.create(imageUrl, linkUrl, startDateTime, endDateTime, platformType));

        // when
        List<Popup> findPopups = popupRepository.findActivatedPopupsByPlatform(platformType, startDateTime.plusMinutes(1));

        // then
        assertAll(
            () -> assertThat(findPopups).hasSize(1),
            () -> assertThat(findPopups.get(0).getImageUrl()).isEqualTo(imageUrl),
            () -> assertThat(findPopups.get(0).getLinkUrl()).isEqualTo(linkUrl),
            () -> assertThat(findPopups.get(0).getStartDateTime()).isEqualTo(startDateTime),
            () -> assertThat(findPopups.get(0).getEndDateTime()).isEqualTo(endDateTime),
            () -> assertThat(findPopups.get(0).getPlatformType()).isEqualTo(platformType)
        );
    }

    @Test
    void 날짜_범위안에_속한_팝업을_조회한다_종료_전() {
        // given
        PopupPlatformType platformType = PopupPlatformType.IOS;
        LocalDateTime startDateTime = LocalDateTime.of(2021, 11, 24, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 11, 25, 0, 0);
        String imageUrl = "https://image.url";
        String linkUrl = "https://link.com";

        popupRepository.save(PopupCreator.create(imageUrl, linkUrl, startDateTime, endDateTime, platformType));

        // when
        List<Popup> findPopups = popupRepository.findActivatedPopupsByPlatform(platformType, endDateTime.minusMinutes(1));

        // then
        assertAll(
            () -> assertThat(findPopups).hasSize(1),
            () -> assertThat(findPopups.get(0).getImageUrl()).isEqualTo(imageUrl),
            () -> assertThat(findPopups.get(0).getLinkUrl()).isEqualTo(linkUrl),
            () -> assertThat(findPopups.get(0).getStartDateTime()).isEqualTo(startDateTime),
            () -> assertThat(findPopups.get(0).getEndDateTime()).isEqualTo(endDateTime),
            () -> assertThat(findPopups.get(0).getPlatformType()).isEqualTo(platformType)
        );
    }

}