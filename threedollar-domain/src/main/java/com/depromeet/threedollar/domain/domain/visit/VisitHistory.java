package com.depromeet.threedollar.domain.domain.visit;

import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uni_visit_history_1", columnNames = {"store_id", "userId", "dateOfVisit"})
    },
    indexes = {
        @Index(name = "idx_visit_history_2", columnList = "userId,type"),
        @Index(name = "idx_visit_history_3", columnList = "store_id,type,dateOfVisit")
    }
)
public class VisitHistory extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private VisitType type;

    @Column(nullable = false)
    private LocalDate dateOfVisit;

    @Builder(access = AccessLevel.PACKAGE)
    private VisitHistory(Store store, Long userId, VisitType type, LocalDate dateOfVisit) {
        this.store = store;
        this.userId = userId;
        this.type = type;
        this.dateOfVisit = dateOfVisit;
    }

    public static VisitHistory of(Store store, Long userId, VisitType type, LocalDate dateOfVisit) {
        return VisitHistory.builder()
            .store(store)
            .userId(userId)
            .type(type)
            .dateOfVisit(dateOfVisit)
            .build();
    }

}
