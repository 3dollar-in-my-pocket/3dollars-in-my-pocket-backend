package com.depromeet.threedollar.domain.domain.visit;

import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class VisitHistory extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitType type;

    @Column(nullable = false)
    private LocalDate dateOfVisit;

    @Builder(access = AccessLevel.PACKAGE)
    private VisitHistory(Long storeId, Long userId, VisitType type, LocalDate dateOfVisit) {
        this.storeId = storeId;
        this.userId = userId;
        this.type = type;
        this.dateOfVisit = dateOfVisit;
    }

    public static VisitHistory of(Long storeId, Long userId, VisitType type, LocalDate dateOfVisit) {
        return VisitHistory.builder()
            .storeId(storeId)
            .userId(userId)
            .type(type)
            .dateOfVisit(dateOfVisit)
            .build();
    }

}
