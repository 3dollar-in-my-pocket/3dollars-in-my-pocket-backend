package com.depromeet.threedollar.domain.rds.user.domain.advertisement;

import com.depromeet.threedollar.domain.rds.common.domain.AuditingTimeEntity;
import com.depromeet.threedollar.domain.rds.common.domain.DateTimeInterval;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    indexes = {
        @Index(name = "idx_advertisemment_1", columnList = "positionType,platformType,id,startDateTime")
    }
)
public class Advertisement extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private Advertisement(AdvertisementPositionType positionType, AdvertisementPlatformType platformType, LocalDateTime startDateTime, LocalDateTime endDateTime, AdvertisementDetail detail) {
        this.positionType = positionType;
        this.platformType = platformType;
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
        this.detail = detail;
    }

    public static Advertisement newInstance(AdvertisementPositionType positionType, AdvertisementPlatformType platformType,
                                            LocalDateTime startDateTime, LocalDateTime endDateTime, AdvertisementDetail detail) {
        return Advertisement.builder()
            .positionType(positionType)
            .platformType(platformType)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .detail(detail)
            .build();
    }

    public void update(AdvertisementPositionType positionType, AdvertisementPlatformType platformType, LocalDateTime startDateTime, LocalDateTime endDateTime, AdvertisementDetail detail) {
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
