package com.depromeet.threedollar.domain.domain.medal;

import com.depromeet.threedollar.domain.domain.medal.repository.MedalRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedalRepository extends JpaRepository<Medal, Long>, MedalRepositoryCustom {

}
