package com.depromeet.threedollar.domain.domain.medal.repository;

import com.depromeet.threedollar.domain.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.domain.medal.UserMedalStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.depromeet.threedollar.domain.domain.medal.QUserMedal.userMedal;

@RequiredArgsConstructor
public class UserMedalRepositoryCustomImpl implements UserMedalRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserMedal> findAllActivesByUserIds(List<Long> userIds) {
        return queryFactory.selectFrom(userMedal)
            .where(
                userMedal.user.id.in(userIds),
                userMedal.status.eq(UserMedalStatus.ACTIVE)
            ).fetch();
    }

}
