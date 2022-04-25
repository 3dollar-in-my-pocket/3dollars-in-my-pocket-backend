package com.depromeet.threedollar.domain.rds.user.domain.medal.repository;

import com.depromeet.threedollar.domain.rds.user.domain.medal.Medal;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface MedalRepositoryCustom {

    @Nullable
    Medal findMedalById(Long medalId);

    List<Medal> findAllActiveMedals();

    List<Medal> findAllByConditionType(MedalAcquisitionConditionType conditionType);

}
