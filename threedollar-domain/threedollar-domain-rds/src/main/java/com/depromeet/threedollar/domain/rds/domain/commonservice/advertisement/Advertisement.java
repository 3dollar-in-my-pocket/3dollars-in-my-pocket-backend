package com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.domain.rds.core.model.AuditingTimeEntity;
import com.depromeet.threedollar.domain.rds.core.model.DateTimeInterval;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    indexes = {
        @Index(name = "idx_advertisemment_1", columnList = "applicationType,positionType,platformType,id,startDateTime")
    }
)
public class Advertisement extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private AdvertisementPositionType positionType;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private AdvertisementPlatformType platformType;

    @Embedded
    private DateTimeInterval dateTimeInterval;

    @Embedded
    private AdvertisementDetail detail;

    @Builder(access = AccessLevel.PACKAGE)
    private Advertisement(
        @NotNull ApplicationType applicationType,
        @NotNull AdvertisementPositionType positionType,
        @NotNull AdvertisementPlatformType platformType,
        @NotNull LocalDateTime startDateTime,
        @NotNull LocalDateTime endDateTime,
        @NotNull AdvertisementDetail detail
    ) {
        this.applicationType = applicationType;
        this.positionType = positionType;
        this.platformType = platformType;
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
        this.detail = detail;
    }

    public static Advertisement newInstance(
        @NotNull ApplicationType applicationType,
        @NotNull AdvertisementPositionType positionType,
        @NotNull AdvertisementPlatformType platformType,
        @NotNull LocalDateTime startDateTime,
        @NotNull LocalDateTime endDateTime,
        @NotNull AdvertisementDetail detail
    ) {
        return Advertisement.builder()
            .applicationType(applicationType)
            .positionType(positionType)
            .platformType(platformType)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .detail(detail)
            .build();
    }

    public void update(
        @NotNull AdvertisementPositionType positionType,
        @NotNull AdvertisementPlatformType platformType,
        @NotNull LocalDateTime startDateTime,
        @NotNull LocalDateTime endDateTime,
        @NotNull AdvertisementDetail detail
    ) {
        this.positionType = positionType;
        this.platformType = platformType;
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
        this.detail = detail;
    }

    @NotNull
    public LocalDateTime getStartDateTime() {
        return dateTimeInterval.getStartDateTime();
    }

    @NotNull
    public LocalDateTime getEndDateTime() {
        return dateTimeInterval.getEndDateTime();
    }

}
