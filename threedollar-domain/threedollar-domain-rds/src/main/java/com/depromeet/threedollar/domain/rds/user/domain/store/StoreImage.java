package com.depromeet.threedollar.domain.rds.user.domain.store;

import com.depromeet.threedollar.domain.rds.common.domain.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    indexes = @Index(name = "idx_store_image_1", columnList = "store_id,status")
)
public class StoreImage extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private StoreImageStatus status;

    @Builder(access = AccessLevel.PACKAGE)
    private StoreImage(Store store, Long userId, String url) {
        this.store = store;
        this.userId = userId;
        this.url = url;
        this.status = StoreImageStatus.ACTIVE;
    }

    public static StoreImage newInstance(Store store, Long userId, String imageUrl) {
        return StoreImage.builder()
            .store(store)
            .userId(userId)
            .url(imageUrl)
            .build();
    }

    public void delete() {
        this.status = StoreImageStatus.INACTIVE;
    }

    public void updateUrl(String url) {
        this.url = url;
    }

}
