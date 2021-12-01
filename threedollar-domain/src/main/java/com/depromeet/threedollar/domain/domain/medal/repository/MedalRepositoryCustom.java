package com.depromeet.threedollar.domain.domain.medal.repository;

import com.depromeet.threedollar.domain.domain.medal.Medal;
import com.depromeet.threedollar.domain.domain.medal.MedalAcquisitionConditionType;

import java.util.List;

public interface MedalRepositoryCustom {

    List<Medal> findAllByConditionType(MedalAcquisitionConditionType conditionType);

}
