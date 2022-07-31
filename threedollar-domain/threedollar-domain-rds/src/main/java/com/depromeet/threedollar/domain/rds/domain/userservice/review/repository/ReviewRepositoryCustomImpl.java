package com.depromeet.threedollar.domain.rds.domain.userservice.review.repository;

import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewStatus;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.collection.ReviewCursorPaging;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.LockModeType;
import java.util.List;

import static com.depromeet.threedollar.common.type.CacheType.CacheKey.USER_REVIEWS_COUNTS;
import static com.depromeet.threedollar.domain.rds.core.constants.RDBPackageConstants.PERSISTENCE_LOCK_TIMEOUT;
import static com.depromeet.threedollar.domain.rds.core.support.QuerydslSupport.predicate;
import static com.depromeet.threedollar.domain.rds.domain.userservice.review.QReview.review;
import static com.depromeet.threedollar.domain.rds.domain.userservice.store.QStore.store;

@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Nullable
    @Override
    public Review findReviewById(Long reviewId) {
        return queryFactory.selectFrom(review)
            .where(
                review.id.eq(reviewId),
                review.status.eq(ReviewStatus.POSTED)
            ).fetchOne();
    }

    @Nullable
    @Override
    public Review findReviewByIdAndUserId(Long reviewId, Long userId) {
        return queryFactory.selectFrom(review)
            .where(
                review.id.eq(reviewId),
                review.userId.eq(userId),
                review.status.eq(ReviewStatus.POSTED)
            ).fetchOne();
    }

    @Override
    public List<Review> findAllByStoreId(Long storeId) {
        return queryFactory.selectFrom(review)
            .where(
                review.storeId.eq(storeId),
                review.status.eq(ReviewStatus.POSTED)
            )
            .fetch();
    }

    @Override
    public List<Review> findAllByStoreIdWithLock(Long storeId) {
        return queryFactory.selectFrom(review)
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .setHint(PERSISTENCE_LOCK_TIMEOUT, 3000)
            .where(
                review.storeId.eq(storeId),
                review.status.eq(ReviewStatus.POSTED)
            )
            .fetch();
    }

    @Cacheable(cacheNames = USER_REVIEWS_COUNTS, key = "#userId")
    @Override
    public long countByUserId(Long userId) {
        return queryFactory.select(review.id)
            .from(review)
            .innerJoin(store).on(review.storeId.eq(store.id))
            .where(
                review.userId.eq(userId),
                review.status.eq(ReviewStatus.POSTED)
            )
            .fetchCount();
    }

    @Override
    public ReviewCursorPaging findAllByUserIdUsingCursor(Long userId, @Nullable Long lastStoreId, int size) {
        List<Review> reviews = queryFactory.selectFrom(review)
            .where(
                review.userId.eq(userId),
                review.status.eq(ReviewStatus.POSTED),
                predicate(lastStoreId != null, () -> review.id.lt(lastStoreId))
            )
            .orderBy(review.id.desc())
            .limit(size + 1)
            .fetch();

        return ReviewCursorPaging.of(reviews, size);
    }

}
