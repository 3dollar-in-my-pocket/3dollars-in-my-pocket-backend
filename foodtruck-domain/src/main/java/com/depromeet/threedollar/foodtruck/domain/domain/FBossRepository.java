package com.depromeet.threedollar.foodtruck.domain.domain;

import com.depromeet.threedollar.foodtruck.domain.domain.repository.FBossAccountRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FBossRepository extends JpaRepository<FBossAccount, Long>, FBossAccountRepositoryCustom {

}
