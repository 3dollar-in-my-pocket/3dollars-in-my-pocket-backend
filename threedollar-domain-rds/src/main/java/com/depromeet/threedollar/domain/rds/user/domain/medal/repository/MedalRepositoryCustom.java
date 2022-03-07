package com.depromeet.threedollar.domain.rds.user.domain.medal.repository;

import com.depromeet.threedollar.domain.rds.user.domain.medal.Medal;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionType;

import java.util.List;

public interface MedalRepositoryCustom {

    Medal findMedalById(Long medalId);

    List<Medal> findAllActiveMedals();

    List<Medal> findAllByConditionType(MedalAcquisitionConditionType conditionType);

}
