package com.depromeet.threedollar.domain.domain.user.repository;

import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.LockModeType;

import java.time.LocalDate;

import static com.depromeet.threedollar.domain.domain.user.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // TODO 호환상, name이 유니크 키로 잡혀있지 않아서, Locking Read 처리 중 -> 차후 마이그레이션 이후 유니크 키로 잡도록 고려.
    @Override
    public boolean existsByName(String name) {
        return queryFactory.selectOne()
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .setHint("javax.persistence.lock.timeout", 2000)
            .from(user)
            .where(user.name.eq(name))
            .fetchFirst() != null;
    }

    @Override
    public boolean existsBySocialIdAndSocialType(String socialId, UserSocialType socialType) {
        return queryFactory.selectOne()
            .from(user)
            .where(
                user.socialInfo.socialId.eq(socialId),
                user.socialInfo.socialType.eq(socialType)
            ).fetchFirst() != null;
    }

    @Override
    public User findUserBySocialIdAndSocialType(String socialId, UserSocialType type) {
        return queryFactory.selectFrom(user)
            .where(
                user.socialInfo.socialId.eq(socialId),
                user.socialInfo.socialType.eq(type)
            ).fetchOne();
    }

    @Override
    public User findUserById(Long userId) {
        return queryFactory.selectFrom(user)
            .where(user.id.eq(userId))
            .fetchOne();
    }

    @Override
    public long findUsersCount() {
        return queryFactory.select(user.id)
            .from(user)
            .fetchCount();
    }

    @Override
    public long findUsersCountByDate(LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(user.id)
            .from(user)
            .where(
                user.createdAt.goe(startDate.atStartOfDay()),
                user.createdAt.lt(endDate.atStartOfDay().plusDays(1))
            )
            .fetchCount();
    }

}
