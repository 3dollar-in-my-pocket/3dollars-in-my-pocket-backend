package com.depromeet.threedollar.domain.rds.user.domain.store;

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

import com.depromeet.threedollar.domain.rds.common.domain.AuditingTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    indexes = {
        @Index(name = "idx_store_delete_request_1", columnList = "store_id"),
        @Index(name = "idx_store_delete_request_2", columnList = "userId")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uni_store_delete_request_1", columnNames = {"store_id", "userId"})
    }
)
public class StoreDeleteRequest extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 30)
    @Enumerated(value = EnumType.STRING)
    private DeleteReasonType reason;

    @Builder(access = AccessLevel.PACKAGE)
    private StoreDeleteRequest(Store store, Long userId, DeleteReasonType reason) {
        this.store = store;
        this.userId = userId;
        this.reason = reason;
    }

    public static StoreDeleteRequest of(Store store, Long userId, DeleteReasonType reason) {
        return StoreDeleteRequest.builder()
            .store(store)
            .userId(userId)
            .reason(reason)
            .build();
    }

}
