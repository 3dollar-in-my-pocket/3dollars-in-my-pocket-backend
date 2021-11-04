package com.depromeet.threedollar.domain.domain.storeimage;

import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    indexes = @Index(name = "idx_store_image_1", columnList = "storeId")
)
public class StoreImage extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private StoreImageStatus status;

    @Builder(access = AccessLevel.PACKAGE)
    private StoreImage(Long storeId, Long userId, String url) {
        this.storeId = storeId;
        this.userId = userId;
        this.url = url;
        this.status = StoreImageStatus.ACTIVE;
    }

    public static StoreImage newInstance(Long storeId, Long userId, String imageUrl) {
        return new StoreImage(storeId, userId, imageUrl);
    }

    public void delete() {
        this.status = StoreImageStatus.INACTIVE;
    }

    public void updateUrl(String url) {
        this.url = url;
    }

}
