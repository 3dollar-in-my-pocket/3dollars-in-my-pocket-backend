package com.depromeet.threedollar.domain.boss.domain.account;

import com.depromeet.threedollar.domain.boss.domain.account.repository.BossAccountRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BossAccountRepository extends JpaRepository<BossAccount, Long>, BossAccountRepositoryCustom {

}
