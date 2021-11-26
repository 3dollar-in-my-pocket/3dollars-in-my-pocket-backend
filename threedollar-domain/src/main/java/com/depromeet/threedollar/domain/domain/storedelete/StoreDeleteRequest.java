package com.depromeet.threedollar.domain.domain.storedelete;

import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    indexes = {
        @Index(name = "idx_store_delete_request_1", columnList = "storeId")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uni_store_delete_request_1", columnNames = {"storeId", "userId"})
    }
)
public class StoreDeleteRequest extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 30)
    @Enumerated(value = EnumType.STRING)
    private DeleteReasonType reason;

    private StoreDeleteRequest(Long storeId, Long userId, DeleteReasonType reason) {
        this.storeId = storeId;
        this.userId = userId;
        this.reason = reason;
    }

    public static StoreDeleteRequest of(Long storeId, Long userId, DeleteReasonType reason) {
        return new StoreDeleteRequest(storeId, userId, reason);
    }

}
