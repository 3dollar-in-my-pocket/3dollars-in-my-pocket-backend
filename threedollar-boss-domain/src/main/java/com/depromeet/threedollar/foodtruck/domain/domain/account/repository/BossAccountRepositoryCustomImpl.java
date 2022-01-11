package com.depromeet.threedollar.foodtruck.domain.domain.account.repository;

import com.depromeet.threedollar.foodtruck.domain.domain.account.BossAccount;
import com.depromeet.threedollar.foodtruck.domain.domain.account.BossAccountSocialType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.depromeet.threedollar.foodtruck.domain.domain.account.QBossAccount.bossAccount;

@RequiredArgsConstructor
public class BossAccountRepositoryCustomImpl implements BossAccountRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public BossAccount findBossAccountBySocialTypeAndSocialId(String socialId, BossAccountSocialType socialType) {
        return queryFactory.selectFrom(bossAccount)
            .where(
                bossAccount.socialInfo.socialId.eq(socialId),
                bossAccount.socialInfo.socialType.eq(socialType)
            ).fetchOne();
    }

    @Override
    public BossAccount findBossAccountById(Long bossAccountId) {
        return queryFactory.selectFrom(bossAccount)
            .where(
                bossAccount.id.eq(bossAccountId)
            ).fetchOne();
    }

}
