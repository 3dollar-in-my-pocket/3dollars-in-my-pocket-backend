package com.depromeet.threedollar.domain.user.domain.store;

import com.depromeet.threedollar.domain.common.domain.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class StorePromotion extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 2048)
    private String introduction;

    @Column(length = 2048)
    private String imageUrl;

    @Builder(access = AccessLevel.PACKAGE)
    private StorePromotion(String title, String introduction, @Nullable String imageUrl) {
        this.title = title;
        this.introduction = introduction;
        this.imageUrl = imageUrl;
    }

}
