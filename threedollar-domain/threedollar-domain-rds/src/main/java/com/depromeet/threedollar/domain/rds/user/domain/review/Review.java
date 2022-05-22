package com.depromeet.threedollar.domain.rds.user.domain.review;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.jetbrains.annotations.NotNull;

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
    private ReviewRating rating;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @Builder(access = AccessLevel.PACKAGE)
    private Review(@NotNull Long storeId, @NotNull Long userId, @NotNull String contents, int rating, @NotNull ReviewStatus status) {
        this.storeId = storeId;
        this.userId = userId;
        this.contents = contents;
        this.rating = ReviewRating.of(rating);
        this.status = status;
    }

    public static Review of(@NotNull Long storeId, @NotNull Long userId, @NotNull String contents, int rating) {
        return Review.builder()
            .storeId(storeId)
            .userId(userId)
            .contents(contents)
            .rating(rating)
            .status(ReviewStatus.POSTED)
            .build();
    }

    public void update(@NotNull String contents, int rating) {
        this.contents = contents;
        this.rating = ReviewRating.of(rating);
    }

    public void delete() {
        this.status = ReviewStatus.DELETED;
    }

    public void deleteByAdmin() {
        this.status = ReviewStatus.FILTERED;
    }

    public int getRating() {
        return this.rating.getRating();
    }

}
