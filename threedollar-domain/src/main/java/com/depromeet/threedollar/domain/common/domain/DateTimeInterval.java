package com.depromeet.threedollar.domain.common.domain;

import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
public class DateTimeInterval {

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    public static DateTimeInterval of(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        validateDateTimeInterval(startDateTime, endDateTime);
        return new DateTimeInterval(startDateTime, endDateTime);
    }

    private static void validateDateTimeInterval(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new InvalidException(String.format("시작 날짜(%s) 가 종료 날짜(%s)보다 느릴 수 없습니", startDateTime, endDateTime), ErrorCode.INVALID_DATE_TIME_INTERVAL);
        }
    }

}
