package com.depromeet.threedollar.foodtruck.domain.domain.account;

import com.depromeet.threedollar.foodtruck.domain.domain.account.repository.FBossAccountRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FBossRepository extends JpaRepository<FBossAccount, Long>, FBossAccountRepositoryCustom {

}
