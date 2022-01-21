package com.depromeet.threedollar.domain.user.domain.popup;

import com.depromeet.threedollar.domain.common.domain.AuditingTimeEntity;
import com.depromeet.threedollar.domain.common.domain.DateTimeInterval;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    indexes = {
        @Index(name = "idx_popup_1", columnList = "positionType,platformType,id,startDateTime")
    }
)
public class Popup extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private PopupPositionType positionType;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private PopupPlatformType platformType;

    @Embedded
    private DateTimeInterval dateTimeInterval;

    @Embedded
    private PopupDetail detail;

    @Builder(access = AccessLevel.PACKAGE)
    private Popup(PopupPositionType positionType, PopupPlatformType platformType, @Nullable String title, @Nullable String subTitle,
                  String imageUrl, String linkUrl, @Nullable String bgColor, @Nullable String fontColor, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.positionType = positionType;
        this.platformType = platformType;
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
        this.detail = PopupDetail.of(title, subTitle, imageUrl, linkUrl, bgColor, fontColor);
    }

    public static Popup newInstance(PopupPositionType positionType, PopupPlatformType platformType, @Nullable String title,
                                    @Nullable String subTitle, @Nullable String imageUrl, @Nullable String linkUrl,
                                    @Nullable String bgColor, @Nullable String fontColor, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return Popup.builder()
            .positionType(positionType)
            .platformType(platformType)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .title(title)
            .subTitle(subTitle)
            .bgColor(bgColor)
            .fontColor(fontColor)
            .imageUrl(imageUrl)
            .linkUrl(linkUrl)
            .build();
    }

    public void update(PopupPositionType positionType, PopupPlatformType platformType, @Nullable String title, @Nullable String subTitle,
                       String imageUrl, @Nullable String linkUrl, @Nullable String bgColor, @Nullable String fontColor, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.positionType = positionType;
        this.platformType = platformType;
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
        this.detail = PopupDetail.of(title, subTitle, imageUrl, linkUrl, bgColor, fontColor);
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
