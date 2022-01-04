package com.depromeet.threedollar.foodtruck.domain.domain.repository;

import com.depromeet.threedollar.foodtruck.domain.domain.FBossAccount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.depromeet.threedollar.foodtruck.domain.domain.QFBossAccount.fBossAccount;

@RequiredArgsConstructor
public class FBossAccountRepositoryCustomImpl implements FBossAccountRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public FBossAccount findBossAccountById(Long id) {
        return queryFactory.selectFrom(fBossAccount)
            .where(
                fBossAccount.id.eq(id)
            ).fetchOne();
    }

}
