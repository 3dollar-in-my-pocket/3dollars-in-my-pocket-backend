package com.depromeet.threedollar.domain.rds.common.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private DateTimeInterval(@NotNull LocalDateTime startDateTime, @NotNull LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public static DateTimeInterval of(@NotNull LocalDateTime startDateTime, @NotNull LocalDateTime endDateTime) {
        validateDateTimeInterval(startDateTime, endDateTime);
        return DateTimeInterval.builder()
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();
    }

    private static void validateDateTimeInterval(@NotNull LocalDateTime startDateTime, @NotNull LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new InvalidException(String.format("시작 날짜(%s) 가 종료 날짜(%s)보다 이후 일 수 없습니", startDateTime, endDateTime), ErrorCode.INVALID_DATE_TIME_INTERVAL);
        }
    }

}
