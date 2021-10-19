package com.depromeet.threedollar.domain.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static com.depromeet.threedollar.domain.domain.user.QUser.user;

@RequiredArgsConstructor
public class UserStaticsRepositoryCustomImpl implements UserStaticsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long findUsersCount() {
        return queryFactory.select(user.id)
            .from(user)
            .fetchCount();
    }

    @Override
    public long findUsersCountBetweenDate(LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(user.id)
            .from(user)
            .where(
                user.createdAt.goe(startDate.atStartOfDay()),
                user.createdAt.lt(endDate.atStartOfDay().plusDays(1))
            )
            .fetchCount();
    }

}
