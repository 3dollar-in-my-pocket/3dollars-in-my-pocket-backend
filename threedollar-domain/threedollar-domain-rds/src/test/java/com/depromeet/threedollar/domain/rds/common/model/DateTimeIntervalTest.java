package com.depromeet.threedollar.domain.rds.common.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.depromeet.threedollar.common.exception.model.InvalidException;

class DateTimeIntervalTest {

    @Test
    void 시작날짜가_종료날짜_이후인경우_Validation_Exception_에러가_발생한다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 11, 25, 0, 1);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 11, 25, 0, 0);

        // when & then
        assertThatThrownBy(() -> DateTimeInterval.of(startDateTime, endDateTime)).isInstanceOf(InvalidException.class);
    }

    @Test
    void 시작날짜가_종료날짜_같은경우는_에러가_발생하지_않는다() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2021, 11, 25, 0, 0);

        // when
        DateTimeInterval sut = DateTimeInterval.of(dateTime, dateTime);

        // then
        assertThat(sut.getStartDateTime()).isEqualTo(dateTime);
        assertThat(sut.getEndDateTime()).isEqualTo(dateTime);
    }

    @Test
    void 시작날짜가_종료날짜보다_이전인경우_에러가_발생하지_않는다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 11, 25, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 11, 26, 0, 0);

        // when
        DateTimeInterval sut = DateTimeInterval.of(startDateTime, endDateTime);

        // then
        assertThat(sut.getStartDateTime()).isEqualTo(startDateTime);
        assertThat(sut.getEndDateTime()).isEqualTo(endDateTime);
    }

}
