package com.depromeet.threedollar.foodtruck.domain.domain.account;

import com.depromeet.threedollar.foodtruck.domain.domain.account.repository.BossAccountRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BossAccountRepository extends JpaRepository<BossAccount, Long>, BossAccountRepositoryCustom {

}
