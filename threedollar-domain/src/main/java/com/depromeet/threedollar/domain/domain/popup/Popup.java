package com.depromeet.threedollar.domain.domain.popup;

import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import com.depromeet.threedollar.domain.domain.common.DateTimeInterval;
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

    @Column(nullable = false, length = 2048)
    private String imageUrl;

    @Column(length = 2048)
    private String linkUrl;

    @Column(nullable = false)
    private int priority;

    @Embedded
    private DateTimeInterval dateTimeInterval;

    @Builder(access = AccessLevel.PACKAGE)
    private Popup(PopupPositionType positionType, PopupPlatformType platformType, String imageUrl, String linkUrl, LocalDateTime startDateTime, LocalDateTime endDateTime, int priority) {
        this.positionType = positionType;
        this.platformType = platformType;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
        this.priority = priority;
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
