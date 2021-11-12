package com.depromeet.threedollar.domain.domain.medal.repository;

import com.depromeet.threedollar.domain.domain.medal.UserMedalType;

import java.util.List;

public interface UserMedalRepositoryCustom {

    boolean existsMedalByUserId(Long userId, UserMedalType medalType);

    List<UserMedalType> findAllUserMedalTypeByUserId(Long userId);

}
