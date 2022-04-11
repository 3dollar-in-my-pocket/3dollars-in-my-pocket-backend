package com.depromeet.threedollar.domain.rds.user.domain.medal.repository;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.user.domain.medal.Medal;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionType;

public interface MedalRepositoryCustom {

    @Nullable
    Medal findMedalById(Long medalId);

    List<Medal> findAllActiveMedals();

    List<Medal> findAllByConditionType(MedalAcquisitionConditionType conditionType);

}
