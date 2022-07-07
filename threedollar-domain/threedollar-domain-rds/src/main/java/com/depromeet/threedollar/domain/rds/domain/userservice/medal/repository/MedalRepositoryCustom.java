package com.depromeet.threedollar.domain.rds.domain.userservice.medal.repository;

import com.depromeet.threedollar.domain.rds.domain.userservice.medal.Medal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalAcquisitionConditionType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface MedalRepositoryCustom {

    @Nullable
    Medal findMedalById(Long medalId);

    List<Medal> findAllActiveMedals();

    List<Medal> findAllByConditionType(MedalAcquisitionConditionType conditionType);

}
