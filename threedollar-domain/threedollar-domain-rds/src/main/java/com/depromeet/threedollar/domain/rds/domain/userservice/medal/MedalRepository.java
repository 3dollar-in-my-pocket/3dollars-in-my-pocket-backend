package com.depromeet.threedollar.domain.rds.domain.userservice.medal;

import com.depromeet.threedollar.domain.rds.domain.userservice.medal.repository.MedalRepositoryCustom;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.repository.statistics.MedalStatisticsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedalRepository extends JpaRepository<Medal, Long>, MedalRepositoryCustom, MedalStatisticsRepositoryCustom {

}
