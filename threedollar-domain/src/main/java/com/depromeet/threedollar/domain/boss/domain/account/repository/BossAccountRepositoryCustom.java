package com.depromeet.threedollar.domain.boss.domain.account.repository;

import com.depromeet.threedollar.domain.boss.domain.account.BossAccount;
import com.depromeet.threedollar.domain.boss.domain.account.BossAccountSocialType;

public interface BossAccountRepositoryCustom {

    BossAccount findBossAccountBySocialTypeAndSocialId(String socialId, BossAccountSocialType socialType);

    BossAccount findBossAccountById(Long bossAccountId);

}
