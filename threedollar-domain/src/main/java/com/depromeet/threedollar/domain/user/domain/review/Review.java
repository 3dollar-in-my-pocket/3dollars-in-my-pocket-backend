package com.depromeet.threedollar.domain.user.domain.review;

import com.depromeet.threedollar.domain.common.domain.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    indexes = {
        @Index(name = "idx_review_1", columnList = "storeId,status"),
        @Index(name = "idx_review_2", columnList = "userId,status")
    }
)
public class Review extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 200)
    private String contents;

    @Embedded
    private Rating rating;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    private Review(Long storeId, Long userId, String contents, int rating) {
        this.storeId = storeId;
        this.userId = userId;
        this.contents = contents;
        this.rating = Rating.of(rating);
        this.status = ReviewStatus.POSTED;
    }

    public static Review of(Long storeId, Long userId, String contents, int rating) {
        return new Review(storeId, userId, contents, rating);
    }

    public void update(String contents, int rating) {
        this.contents = contents;
        this.rating = Rating.of(rating);
    }

    public void delete() {
        this.status = ReviewStatus.DELETED;
    }

    public int getRating() {
        return this.rating.getRating();
    }

}
