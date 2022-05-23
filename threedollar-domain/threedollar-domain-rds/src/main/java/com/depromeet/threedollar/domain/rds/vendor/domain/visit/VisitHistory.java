package com.depromeet.threedollar.domain.rds.vendor.domain.visit;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.domain.rds.common.domain.AuditingTimeEntity;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.Store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uni_visit_history_1", columnNames = {"store_id", "userId", "dateOfVisit"})
    },
    indexes = {
        @Index(name = "idx_visit_history_2", columnList = "userId,type"),
        @Index(name = "idx_visit_history_3", columnList = "store_id,type,dateOfVisit"),
        @Index(name = "idx_visit_history_4", columnList = "store_id,id,userId,dateOfVisit")
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
    private VisitHistory(@NotNull Store store, @NotNull Long userId, @NotNull VisitType type, @NotNull LocalDate dateOfVisit) {
        this.store = store;
        this.userId = userId;
        this.type = type;
        this.dateOfVisit = dateOfVisit;
    }

    public static VisitHistory of(@NotNull Store store, @NotNull Long userId, @NotNull VisitType type, @NotNull LocalDate dateOfVisit) {
        return VisitHistory.builder()
            .store(store)
            .userId(userId)
            .type(type)
            .dateOfVisit(dateOfVisit)
            .build();
    }

}
