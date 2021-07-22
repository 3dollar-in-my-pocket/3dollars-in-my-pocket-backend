package com.depromeet.threedollar.domain.domain.user.repository;

import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.depromeet.threedollar.domain.domain.user.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public User findUserBySocialIdAndSocialType(String socialId, UserSocialType type) {
        return queryFactory.selectFrom(user)
            .where(
                user.socialInfo.socialId.eq(socialId),
                user.socialInfo.socialType.eq(type)
            ).fetchOne();
    }

    @Override
    public User findUserByName(String name) {
        return queryFactory.selectFrom(user)
            .where(
                user.name.eq(name)
            ).fetchOne();
    }

    @Override
    public User findUserById(Long userId) {
        return queryFactory.selectFrom(user)
            .where(
                user.id.eq(userId)
            ).fetchOne();
    }

    @Override
    public User findUserByIdAndSocialType(Long userId, UserSocialType socialType) {
        return queryFactory.selectFrom(user)
            .where(
                user.id.eq(userId),
                user.socialInfo.socialType.eq(socialType)
            ).fetchOne();
    }

}
