package com.depromeet.threedollar.domain.rds.user.domain.medal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.user.domain.medal.repository.MedalRepositoryCustom;
import com.depromeet.threedollar.domain.rds.user.domain.medal.repository.MedalStatisticsRepositoryCustom;

public interface MedalRepository extends JpaRepository<Medal, Long>, MedalRepositoryCustom, MedalStatisticsRepositoryCustom {

}
