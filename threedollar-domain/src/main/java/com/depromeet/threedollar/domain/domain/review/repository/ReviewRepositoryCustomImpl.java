package com.depromeet.threedollar.domain.domain.review.repository;

import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.review.ReviewStatus;
import com.depromeet.threedollar.domain.domain.review.projection.QReviewWithWriterProjection;
import com.depromeet.threedollar.domain.domain.review.projection.ReviewWithWriterProjection;
import com.depromeet.threedollar.domain.domain.store.StoreStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.LockModeType;
import java.util.List;

import static com.depromeet.threedollar.domain.config.cache.CacheType.CacheKey.USER_REVIEWS_COUNTS;
import static com.depromeet.threedollar.domain.domain.store.QStore.store;
import static com.depromeet.threedollar.domain.domain.user.QUser.user;
import static com.depromeet.threedollar.domain.domain.review.QReview.review;

@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

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
    public List<Review> findAllByStoreIdWithLock(Long storeId) {
        return queryFactory.selectFrom(review)
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .setHint("javax.persistence.lock.timeout", 2000)
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

    @Deprecated
    @Override
    public long findActiveCountsByUserId(Long userId) {
        return queryFactory.select(review.id)
            .from(review)
            .innerJoin(user).on(review.userId.eq(user.id))
            .innerJoin(store).on(review.storeId.eq(store.id))
            .where(
                review.userId.eq(userId),
                review.status.eq(ReviewStatus.POSTED),
                store.status.eq(StoreStatus.ACTIVE)
            )
            .fetchCount();
    }

    @Override
    public List<ReviewWithWriterProjection> findAllWithCreatorByStoreId(Long storeId) {
        return queryFactory.select(new QReviewWithWriterProjection(
            review.id,
            review.rating.rating,
            review.contents,
            review.createdAt,
            review.updatedAt,
            review.storeId,
            user.id,
            user.name,
            user.socialInfo.socialType
        ))
            .from(review)
            .leftJoin(user).on(review.userId.eq(user.id))
            .where(
                review.storeId.eq(storeId),
                review.status.eq(ReviewStatus.POSTED)
            ).fetch();
    }

    @Override
    public List<ReviewWithWriterProjection> findAllByUserIdWithScroll(Long userId, Long lastStoreId, int size) {
        List<Long> reviewIds = queryFactory.select(review.id)
            .from(review)
            .innerJoin(user).on(review.userId.eq(user.id))
            .innerJoin(store).on(review.storeId.eq(store.id))
            .where(
                review.userId.eq(userId),
                review.status.eq(ReviewStatus.POSTED),
                lessThanId(lastStoreId)
            )
            .orderBy(review.id.desc())
            .limit(size)
            .fetch();

        return findReviewWithWriterProjectionsByIds(reviewIds);
    }

    @Deprecated
    @Override
    public List<ReviewWithWriterProjection> findAllActiveByUserIdWithScroll(Long userId, Long lastStoreId, int size) {
        List<Long> reviewIds = queryFactory.select(review.id)
            .from(review)
            .innerJoin(user).on(review.userId.eq(user.id))
            .innerJoin(store).on(review.storeId.eq(store.id))
            .where(
                review.userId.eq(userId),
                review.status.eq(ReviewStatus.POSTED),
                store.status.eq(StoreStatus.ACTIVE),
                lessThanId(lastStoreId)
            )
            .orderBy(review.id.desc())
            .limit(size)
            .fetch();

        return findReviewWithWriterProjectionsByIds(reviewIds);
    }

    private List<ReviewWithWriterProjection> findReviewWithWriterProjectionsByIds(List<Long> reviewIds) {
        return queryFactory.select(new QReviewWithWriterProjection(
            review.id,
            review.rating.rating,
            review.contents,
            review.createdAt,
            review.updatedAt,
            review.storeId,
            user.id,
            user.name,
            user.socialInfo.socialType
        ))
            .from(review)
            .innerJoin(user).on(review.userId.eq(user.id))
            .where(
                review.id.in(reviewIds)
            )
            .orderBy(review.id.desc())
            .fetch();
    }

    private BooleanExpression lessThanId(Long lastStoreId) {
        if (lastStoreId == null) {
            return null;
        }
        return review.id.lt(lastStoreId);
    }

}
