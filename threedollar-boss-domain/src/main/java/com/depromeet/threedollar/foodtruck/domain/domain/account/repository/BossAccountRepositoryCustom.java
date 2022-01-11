package com.depromeet.threedollar.foodtruck.domain.domain.account.repository;

import com.depromeet.threedollar.foodtruck.domain.domain.account.BossAccount;
import com.depromeet.threedollar.foodtruck.domain.domain.account.BossAccountSocialType;

public interface BossAccountRepositoryCustom {

    BossAccount findBossAccountBySocialTypeAndSocialId(String socialId, BossAccountSocialType socialType);

    BossAccount findBossAccountById(Long bossAccountId);

}
