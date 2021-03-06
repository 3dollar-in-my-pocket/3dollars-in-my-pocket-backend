package com.depromeet.threedollar.domain.rds.domain.userservice.store.repository;

import com.depromeet.threedollar.domain.rds.core.support.OrderByNull;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreStatus;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.collection.StoreCursorPaging;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.collection.StoreWithMenuCursorPaging;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.QStoreWithMenuProjection;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.QStoreWithMenuProjection_MenuProjection;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.StoreWithMenuProjection;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

import static com.depromeet.threedollar.common.type.CacheType.CacheKey.USER_STORES_COUNTS;
import static com.depromeet.threedollar.domain.rds.core.support.QuerydslSupport.predicate;
import static com.depromeet.threedollar.domain.rds.domain.userservice.store.QMenu.menu;
import static com.depromeet.threedollar.domain.rds.domain.userservice.store.QStore.store;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.dsl.MathExpressions.acos;
import static com.querydsl.core.types.dsl.MathExpressions.cos;
import static com.querydsl.core.types.dsl.MathExpressions.radians;
import static com.querydsl.core.types.dsl.MathExpressions.sin;

@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Nullable
    @Override
    public Store findStoreById(Long storeId) {
        return queryFactory.selectFrom(store)
            .where(
                store.id.eq(storeId),
                store.status.eq(StoreStatus.ACTIVE)
            ).fetchOne();
    }

    /**
     * OneToMany????????? ?????? FetchJoin ??????.
     * Store??? ???????????? [menu, appearanceDays, payments]??? oneToMany??? ???????????? N+1??? ???????????? ?????????, menu??? fetchJoin??? ????????????
     * default_batch_fetch_size: 1000 ?????? ????????????
     * 1000??? ????????? WHERE store_id IN (...)?????? ???????????? N+1 ????????? ???????????? ???.
     */
    @Nullable
    @Override
    public Store findStoreByIdFetchJoinMenu(Long storeId) {
        return queryFactory.selectFrom(store)
            .innerJoin(store.menus, menu).fetchJoin()
            .where(
                store.id.eq(storeId),
                store.status.eq(StoreStatus.ACTIVE)
            ).fetchOne();
    }

    @Override
    public List<Store> findAllByIds(List<Long> storeIds) {
        return queryFactory.selectFrom(store).distinct()
            .innerJoin(store.menus, menu).fetchJoin()
            .where(
                store.id.in(storeIds)
            )
            .orderBy(store.id.desc())
            .fetch();
    }

    @Cacheable(cacheNames = USER_STORES_COUNTS, key = "#userId")
    @Override
    public long countByUserId(Long userId) {
        return queryFactory.select(store.id).distinct()
            .from(store)
            .innerJoin(menu).on(menu.store.id.eq(store.id))
            .where(
                store.userId.eq(userId)
            )
            .fetchCount();
    }

    @Override
    public StoreCursorPaging findAllUsingCursor(@Nullable Long lastStoreId, int size) {
        List<Long> storeIds = queryFactory.select(store.id).distinct()
            .from(store)
            .innerJoin(menu).on(menu.store.id.eq(store.id))
            .where(
                predicate(lastStoreId != null, () -> store.id.lt(lastStoreId))
            )
            .orderBy(store.id.desc())
            .limit(size + 1L)
            .fetch();

        return StoreCursorPaging.of(findAllByIds(storeIds), size);
    }

    /**
     * NoOffset ????????? ????????? ?????? ??????????????????
     * 1:N ???????????? fetchJoin??? ?????? ??????, limit??? ????????? ??? ?????? ????????? ????????? ???????????? ???. (firstResult/maxResults specified with collection fetch; applying in memory)
     * ??? ????????? ???????????? ????????? StoreId ????????? ?????? ??? ?????????????????? ???????????? ??????.
     */
    @Override
    public StoreWithMenuCursorPaging findAllByUserIdUsingCursor(Long userId, @Nullable Long lastStoreId, int size) {
        List<Long> storeIds = queryFactory.select(store.id).distinct()
            .from(store)
            .innerJoin(menu).on(menu.store.id.eq(store.id))
            .where(
                store.userId.eq(userId),
                predicate(lastStoreId != null, () -> store.id.lt(lastStoreId))
            )
            .orderBy(store.id.desc())
            .limit(size + 1L)
            .fetch();

        List<StoreWithMenuProjection> stores = queryFactory
            .from(store)
            .innerJoin(menu).on(menu.store.id.eq(store.id))
            .where(
                store.id.in(storeIds)
            )
            .orderBy(store.id.desc())
            .transform(groupBy(store.id).list(getStoreProjection()));

        return StoreWithMenuCursorPaging.of(stores, size);
    }

    /**
     * ?????? 1km = 1 / 109.95???
     * ?????? 1km = 1 / 88.74???
     * 1. ?????? ?????? ???????????? ????????? ??? ?????? ????????? ???????????????
     * 2. ??? ????????? ????????? ??????????????? ?????? ???????????? ???????????? ??????.
     * (????????? ????????? + Range ????????????)
     */
    @Override
    public List<StoreWithMenuProjection> findStoresByLocationLessThanDistance(double latitude, double longitude, double distance) {
        List<Long> storeIds = queryFactory.select(store.id)
            .from(store)
            .where(
                store.location.latitude.goe(latitude - distance / 100),
                store.location.latitude.loe(latitude + distance / 100),
                store.location.longitude.goe(longitude - distance / 80),
                store.location.longitude.loe(longitude + distance / 80),
                Expressions.predicate(Ops.LOE, Expressions.asNumber(getDistanceExpression(latitude, longitude)), Expressions.asNumber(distance))
            )
            .groupBy(store.id, store.location.latitude, store.location.longitude)
            .orderBy(OrderByNull.DEFAULT)
            .fetch();

        return queryFactory
            .from(store)
            .innerJoin(menu).on(menu.store.id.eq(store.id))
            .where(
                store.id.in(storeIds),
                store.status.eq(StoreStatus.ACTIVE)
            )
            .transform(groupBy(store.id).list(getStoreProjection()));
    }

    private QStoreWithMenuProjection getStoreProjection() {
        return new QStoreWithMenuProjection(
            store.id,
            store.userId,
            store.location.latitude,
            store.location.longitude,
            store.name,
            store.rating,
            store.status,
            GroupBy.list(new QStoreWithMenuProjection_MenuProjection(menu.name, menu.price, menu.category)),
            store.createdAt,
            store.updatedAt
        );
    }

    /**
     * ?????? (latitude), ?????? (longitude)??? ??????????????? ?????? ?????? ??????.
     * 6371 * acos(cos(radians(:latitude)) * cos(radians(store.latitude)) * cos(radians(store.longitude)
     * - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(store.latitude)))) as distance
     */
    private NumberExpression<Double> getDistanceExpression(double latitude, double longitude) {
        return acos(sin(radians(Expressions.constant(latitude)))
            .multiply(sin(radians(store.location.latitude)))
            .add(cos(radians(Expressions.constant(latitude)))
                .multiply(cos(radians(store.location.latitude)))
                .multiply(cos(radians(Expressions.constant(longitude)).subtract(radians(store.location.longitude))))
            )).multiply(6371);
    }

    @Override
    public boolean existsStoreAroundInDistance(double latitude, double longitude, double distance) {
        return queryFactory.selectOne()
            .from(store)
            .where(
                store.location.latitude.goe(latitude - distance / 100),
                store.location.latitude.loe(latitude + distance / 100),
                store.location.longitude.goe(longitude - distance / 80),
                store.location.longitude.loe(longitude + distance / 80),
                Expressions.predicate(Ops.LOE, Expressions.asNumber(getDistanceExpression(latitude, longitude)), Expressions.asNumber(distance))
            )
            .groupBy(store.id, store.location.latitude, store.location.longitude)
            .orderBy(OrderByNull.DEFAULT)
            .fetchFirst() != null;
    }

}
