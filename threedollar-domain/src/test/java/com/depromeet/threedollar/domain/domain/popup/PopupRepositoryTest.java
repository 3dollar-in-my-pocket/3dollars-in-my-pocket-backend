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
        LocalDateTime startDateTime = LocalDateTime.of(2021, 11, 25, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 11, 26, 0, 0);
        String imageUrl = "https://image.url";
        String linkUrl = "https://link.com";

        popupRepository.save(PopupCreator.create(imageUrl, linkUrl, startDateTime, endDateTime));

        // when
        List<Popup> findPopups = popupRepository.findActivatedPopups(startDateTime.plusMinutes(1));

        // then
        assertAll(
            () -> assertThat(findPopups).hasSize(1),
            () -> assertThat(findPopups.get(0).getImageUrl()).isEqualTo(imageUrl),
            () -> assertThat(findPopups.get(0).getLinkUrl()).isEqualTo(linkUrl),
            () -> assertThat(findPopups.get(0).getStartDateTime()).isEqualTo(startDateTime),
            () -> assertThat(findPopups.get(0).getEndDateTime()).isEqualTo(endDateTime),
            () -> assertThat(findPopups.get(0).getStatus()).isEqualTo(PopupStatus.ACTIVE)
        );
    }

    @Test
    void 날짜_범위안에_속한_팝업을_조회한다_종료_전() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 11, 24, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 11, 25, 0, 0);
        String imageUrl = "https://image.url";
        String linkUrl = "https://link.com";

        popupRepository.save(PopupCreator.create(imageUrl, linkUrl, startDateTime, endDateTime));

        // when
        List<Popup> findPopups = popupRepository.findActivatedPopups(endDateTime.minusMinutes(1));

        // then
        assertAll(
            () -> assertThat(findPopups).hasSize(1),
            () -> assertThat(findPopups.get(0).getImageUrl()).isEqualTo(imageUrl),
            () -> assertThat(findPopups.get(0).getLinkUrl()).isEqualTo(linkUrl),
            () -> assertThat(findPopups.get(0).getStartDateTime()).isEqualTo(startDateTime),
            () -> assertThat(findPopups.get(0).getEndDateTime()).isEqualTo(endDateTime),
            () -> assertThat(findPopups.get(0).getStatus()).isEqualTo(PopupStatus.ACTIVE)
        );
    }

    @Test
    void 조건에_맞는_팝업이_여러개인경우_가장_최신에_등록된_팝업부터_반환된다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 11, 24, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 11, 25, 0, 0);

        Popup popup1 = PopupCreator.create("https://image1.url", "https://link1.com", startDateTime, endDateTime);
        Popup popup2 = PopupCreator.create("https://image2.url", "https://link2.com", startDateTime, endDateTime);

        popupRepository.saveAll(List.of(popup1, popup2));

        // when
        List<Popup> findPopups = popupRepository.findActivatedPopups(endDateTime.minusMinutes(1));

        // then
        assertAll(
            () -> assertThat(findPopups).hasSize(1),
            () -> assertThat(findPopups.get(0).getImageUrl()).isEqualTo(popup2.getImageUrl())
        );
    }

    @Test
    void 비활성화된_팝업은_반환되지_않는다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 11, 24, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 11, 25, 0, 0);

        popupRepository.save(PopupCreator.createInActive("https://image.url", "https://link.com", startDateTime, endDateTime));

        // when
        List<Popup> findPopups = popupRepository.findActivatedPopups(endDateTime.minusMinutes(1));

        // then
        assertThat(findPopups).isEmpty();
    }

}
