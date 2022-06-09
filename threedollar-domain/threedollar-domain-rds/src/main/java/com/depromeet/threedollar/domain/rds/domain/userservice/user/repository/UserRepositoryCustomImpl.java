package com.depromeet.threedollar.domain.rds.domain.userservice.user.repository;

import static com.depromeet.threedollar.domain.rds.core.constants.RDBPackageConstants.PERSISTENCE_LOCK_TIMEOUT;
import static com.depromeet.threedollar.domain.rds.domain.userservice.medal.QMedal.medal;
import static com.depromeet.threedollar.domain.rds.domain.userservice.medal.QMedalAcquisitionCondition.medalAcquisitionCondition;
import static com.depromeet.threedollar.domain.rds.domain.userservice.medal.QUserMedal.userMedal;
import static com.depromeet.threedollar.domain.rds.domain.userservice.user.QUser.user;

import java.util.List;

import javax.persistence.LockModeType;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 호환상 name이 유니크 키로 잡혀있지 않아서, lost update 방지를 위해 Locking Read 처리 중 -> 차후 마이그레이션 이후 유니크 키로 잡도록 고려.
    @Override
    public boolean existsByName(String name) {
        return queryFactory.selectOne()
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .setHint(PERSISTENCE_LOCK_TIMEOUT, 3000)
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

    @Nullable
    @Override
    public User findUserBySocialIdAndSocialType(String socialId, UserSocialType type) {
        return queryFactory.selectFrom(user)
            .where(
                user.socialInfo.socialId.eq(socialId),
                user.socialInfo.socialType.eq(type)
            ).fetchOne();
    }

    @Nullable
    @Override
    public User findUserById(Long userId) {
        return queryFactory.selectFrom(user)
            .leftJoin(user.userMedals, userMedal).fetchJoin()
            .leftJoin(userMedal.medal, medal).fetchJoin()
            .leftJoin(medal.acquisitionCondition, medalAcquisitionCondition).fetchJoin()
            .where(user.id.eq(userId))
            .fetchOne();
    }

    @Override
    public List<User> findAllByUserId(List<Long> userIds) {
        return queryFactory.selectFrom(user).distinct()
            .leftJoin(user.userMedals, userMedal).fetchJoin()
            .leftJoin(userMedal.medal, medal).fetchJoin()
            .leftJoin(medal.acquisitionCondition, medalAcquisitionCondition).fetchJoin()
            .where(
                user.id.in(userIds)
            ).fetch();
    }

}
