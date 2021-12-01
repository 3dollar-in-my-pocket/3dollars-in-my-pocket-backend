package com.depromeet.threedollar.domain.domain.medal.repository;

import com.depromeet.threedollar.domain.domain.medal.UserMedal;

import java.util.List;

public interface UserMedalRepositoryCustom {

    List<UserMedal> findAllActivesByUserIds(List<Long> userIds);

}
