package com.depromeet.threedollar.domain.rds.domain.userservice.store;

import com.depromeet.threedollar.domain.rds.core.model.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    indexes = @Index(name = "idx_store_image_1", columnList = "storeId,status")
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
    private StoreImage(@NotNull Long storeId, @NotNull Long userId, @NotNull String url, @NotNull StoreImageStatus status) {
        this.storeId = storeId;
        this.userId = userId;
        this.url = url;
        this.status = status;
    }

    public static StoreImage newInstance(@NotNull Long storeId, @NotNull Long userId, @NotNull String imageUrl) {
        return StoreImage.builder()
            .storeId(storeId)
            .userId(userId)
            .url(imageUrl)
            .status(StoreImageStatus.ACTIVE)
            .build();
    }

    public void delete() {
        this.status = StoreImageStatus.INACTIVE;
    }

    public void updateUrl(@NotNull String url) {
        this.url = url;
    }

}
