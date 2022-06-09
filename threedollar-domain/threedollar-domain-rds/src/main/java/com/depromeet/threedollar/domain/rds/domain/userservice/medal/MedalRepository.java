package com.depromeet.threedollar.domain.rds.domain.userservice.medal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.domain.userservice.medal.repository.MedalRepositoryCustom;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.repository.statistics.MedalStatisticsRepositoryCustom;

public interface MedalRepository extends JpaRepository<Medal, Long>, MedalRepositoryCustom, MedalStatisticsRepositoryCustom {

}
