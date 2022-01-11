package com.depromeet.threedollar.domain.user.domain.medal.repository;

import com.depromeet.threedollar.domain.user.domain.medal.Medal;
import com.depromeet.threedollar.domain.user.domain.medal.MedalAcquisitionConditionType;

import java.util.List;

public interface MedalRepositoryCustom {

    List<Medal> findAllActiveMedals();

    List<Medal> findAllByConditionType(MedalAcquisitionConditionType conditionType);

}
