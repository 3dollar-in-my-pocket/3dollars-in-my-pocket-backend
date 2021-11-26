package com.depromeet.threedollar.domain.domain.popup;

import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import com.depromeet.threedollar.domain.domain.common.DateTimeInterval;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    indexes = {
        @Index(name = "idx_popup_1", columnList = "platformType,startDateTime,endDateTime")
    }
)
public class Popup extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private PopupPlatformType platformType;

    @Column(nullable = false, length = 2048)
    private String imageUrl;

    @Column(length = 2048)
    private String linkUrl;

    @Embedded
    private DateTimeInterval dateTimeInterval;

    @Builder(access = AccessLevel.PACKAGE)
    private Popup(PopupPlatformType platformType, String imageUrl, String linkUrl, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.platformType = platformType;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
    }

    public LocalDateTime getStartDateTime() {
        return dateTimeInterval.getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return dateTimeInterval.getEndDateTime();
    }

}
