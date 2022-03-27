package com.depromeet.threedollar.domain.rds.user.domain.store.repository;

import com.depromeet.threedollar.domain.rds.common.support.OrderByNull;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreStatus;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

import static com.depromeet.threedollar.common.type.CacheType.CacheKey.USER_STORES_COUNTS;
import static com.depromeet.threedollar.domain.rds.user.domain.store.QMenu.menu;
import static com.depromeet.threedollar.domain.rds.user.domain.store.QStore.store;
import static com.querydsl.core.types.dsl.MathExpressions.*;

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
     * OneToMany에서는 다중 FetchJoin 불가.
     * Store를 조회할때 [menu, appearanceDays, payments]를 oneToMany로 조회해서 N+1가 발생하는 이슈로, menu만 fetchJoin을 걸어두고
     * default_batch_fetch_size: 1000 으로 설정하며
     * 1000개 칼럼씩 WHERE store_id IN (...)으로 조회해서 N+1 문제를 해결하는 중.
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

    @Cacheable(key = "#userId", value = USER_STORES_COUNTS)
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
    public List<Store> findAllUsingCursor(Long lastStoreId, int size) {
        List<Long> storeIds = queryFactory.select(store.id).distinct()
            .from(store)
            .innerJoin(menu).on(menu.store.id.eq(store.id))
            .where(
                lessThanId(lastStoreId)
            )
            .orderBy(store.id.desc())
            .limit(size)
            .fetch();

        return findAllByIds(storeIds);
    }

    /**
     * NoOffset 방식의 스크롤 기반 페이지네이션
     * 1:N 관계에서 fetchJoin을 하게 되면, limit를 사용할 수 없어 메모리 상에서 계산하게 됨. (firstResult/maxResults specified with collection fetch; applying in memory)
     * 이 문제를 해결하기 위해서 StoreId 리스트 조회 후 페치조인하는 방식으로 조회.
     */
    @Override
    public List<Store> findAllByUserIdUsingCursor(Long userId, Long lastStoreId, int size) {
        List<Long> storeIds = queryFactory.select(store.id).distinct()
            .from(store)
            .innerJoin(menu).on(menu.store.id.eq(store.id))
            .where(
                store.userId.eq(userId),
                lessThanId(lastStoreId)
            )
            .orderBy(store.id.desc())
            .limit(size)
            .fetch();

        return findAllByIds(storeIds);
    }

    private BooleanExpression lessThanId(Long lastStoreId) {
        if (lastStoreId == null) {
            return null;
        }
        return store.id.lt(lastStoreId);
    }

    /**
     * 위도 1km = 1 / 109.95도
     * 경도 1km = 1 / 88.74도
     * 1. 해당 거리 범위내에 도달할 수 있는 거리를 필터링해서
     * 2. 그 안에서 커버링 인덱스해서 특정 거리내의 가게들을 조회.
     * (커버링 인덱스 + Range 실행계획)
     */
    @Override
    public List<Store> findStoresByLocationLessThanDistance(double latitude, double longitude, double distance) {
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

        return queryFactory.selectFrom(store).distinct()
            .innerJoin(store.menus, menu).fetchJoin()
            .where(
                store.id.in(storeIds),
                store.status.eq(StoreStatus.ACTIVE)
            )
            .fetch();
    }

    /**
     * 위도 (latitude), 경도 (longitude)가 주어졌을때 거리 계산 공식.
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
