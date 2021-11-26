package com.depromeet.threedollar.domain.domain.medal;

import com.depromeet.threedollar.domain.domain.medal.repository.UserMedalRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMedalRepository extends JpaRepository<UserMedal, Long>, UserMedalRepositoryCustom {

}
