package com.depromeet.threedollar.domain.rds.user.domain.storedelete;

import com.depromeet.threedollar.domain.rds.common.domain.AuditingTimeEntity;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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