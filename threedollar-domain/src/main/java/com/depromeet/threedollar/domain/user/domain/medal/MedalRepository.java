package com.depromeet.threedollar.domain.user.domain.medal;

import com.depromeet.threedollar.domain.user.domain.medal.repository.MedalRepositoryCustom;
import com.depromeet.threedollar.domain.user.domain.medal.repository.MedalStatisticsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedalRepository extends JpaRepository<Medal, Long>, MedalRepositoryCustom, MedalStatisticsRepositoryCustom {

}
