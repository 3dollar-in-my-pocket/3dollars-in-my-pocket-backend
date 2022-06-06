package com.depromeet.threedollar.domain.rds.domain.userservice.user.repository;

import static com.depromeet.threedollar.domain.rds.user.domain.user.QUser.user;

import java.time.LocalDate;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserStatisticsRepositoryCustomImpl implements UserStatisticsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countAllUsers() {
        return queryFactory.select(user.id)
            .from(user)
            .fetchCount();
    }

    @Override
    public long countUsersBetweenDate(LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(user.id)
            .from(user)
            .where(
                user.createdAt.goe(startDate.atStartOfDay()),
                user.createdAt.lt(endDate.atStartOfDay().plusDays(1))
            )
            .fetchCount();
    }

}
