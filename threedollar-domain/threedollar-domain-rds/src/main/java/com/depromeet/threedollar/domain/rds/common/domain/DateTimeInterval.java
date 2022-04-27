package com.depromeet.threedollar.domain.rds.common.domain;

import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DateTimeInterval {

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Builder(access = AccessLevel.PRIVATE)
    private DateTimeInterval(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public static DateTimeInterval of(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        validateDateTimeInterval(startDateTime, endDateTime);
        return DateTimeInterval.builder()
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();
    }

    private static void validateDateTimeInterval(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new InvalidException(String.format("시작 날짜(%s) 가 종료 날짜(%s)보다 이후 일 수 없습니", startDateTime, endDateTime), ErrorCode.INVALID_DATE_TIME_INTERVAL);
        }
    }

}
