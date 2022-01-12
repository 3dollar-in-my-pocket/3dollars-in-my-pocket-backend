package com.depromeet.threedollar.domain.boss.domain.account.repository;

import com.depromeet.threedollar.domain.boss.domain.account.BossAccount;
import com.depromeet.threedollar.domain.boss.domain.account.BossAccountSocialType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.depromeet.threedollar.domain.boss.domain.account.QBossAccount.*;

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
