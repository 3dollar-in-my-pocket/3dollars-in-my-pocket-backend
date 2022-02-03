package com.depromeet.threedollar.domain.user.domain.review.repository;

import com.depromeet.threedollar.domain.user.domain.review.Review;
import com.depromeet.threedollar.domain.user.domain.review.ReviewStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.LockModeType;
import java.util.List;

import static com.depromeet.threedollar.common.type.CacheType.CacheKey.USER_REVIEWS_COUNTS;
import static com.depromeet.threedollar.domain.user.domain.review.QReview.review;
import static com.depromeet.threedollar.domain.user.domain.store.QStore.store;

@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

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
            .setHint("javax.persistence.lock.timeout", 3000)
            .where(
                review.storeId.eq(storeId),
                review.status.eq(ReviewStatus.POSTED)
            )
            .fetch();
    }

    @Cacheable(key = "#userId", value = USER_REVIEWS_COUNTS)
    @Override
    public long findCountsByUserId(Long userId) {
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
    public List<Review> findAllByUserIdUsingCursor(Long userId, Long lastStoreId, int size) {
        return queryFactory.selectFrom(review)
            .where(
                review.userId.eq(userId),
                review.status.eq(ReviewStatus.POSTED),
                lessThanId(lastStoreId)
            )
            .orderBy(review.id.desc())
            .limit(size)
            .fetch();
    }

    private BooleanExpression lessThanId(Long lastStoreId) {
        if (lastStoreId == null) {
            return null;
        }
        return review.id.lt(lastStoreId);
    }

}
