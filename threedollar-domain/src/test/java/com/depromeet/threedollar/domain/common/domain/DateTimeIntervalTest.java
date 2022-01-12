package com.depromeet.threedollar.domain.common.domain;

import com.depromeet.threedollar.common.exception.model.ValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DateTimeIntervalTest {

    @Test
    void 시작날짜가_종료날짜_이후인경우_Validation_Exception_에러가_발생한다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 11, 25, 0, 1);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 11, 25, 0, 0);

        // when & then
        assertThatThrownBy(() -> DateTimeInterval.of(startDateTime, endDateTime)).isInstanceOf(ValidationException.class);
    }

    @Test
    void 시작날짜가_종료날짜_같은경우는_OK() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2021, 11, 25, 0, 0);

        // when
        DateTimeInterval sut = DateTimeInterval.of(dateTime, dateTime);

        // then
        assertThat(sut.getStartDateTime()).isEqualTo(dateTime);
        assertThat(sut.getEndDateTime()).isEqualTo(dateTime);
    }

}
