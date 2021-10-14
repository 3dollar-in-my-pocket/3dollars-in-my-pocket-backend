package com.depromeet.threedollar.domain.domain.user.repository;

import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;

import java.time.LocalDate;

public interface UserRepositoryCustom {

    boolean existsByName(String name);

    boolean existsBySocialIdAndSocialType(String socialId, UserSocialType socialType);

    User findUserBySocialIdAndSocialType(String socialId, UserSocialType type);

    User findUserById(Long userId);

    long findUsersCount();

    long findUsersCountByDate(LocalDate startDate, LocalDate endDate);

}
