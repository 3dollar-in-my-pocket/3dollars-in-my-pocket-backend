package com.depromeet.threedollar.domain.rds.domain.userservice.user.repository;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;

public interface UserRepositoryCustom {

    boolean existsUserByName(String name);

    boolean existsUserBySocialIdAndSocialType(String socialId, UserSocialType socialType);

    boolean existsUserById(Long userId);

    @Nullable
    User findUserBySocialIdAndSocialType(String socialId, UserSocialType type);

    @Nullable
    User findUserById(Long userId);

    List<User> findAllByUserId(List<Long> userIds);

}
