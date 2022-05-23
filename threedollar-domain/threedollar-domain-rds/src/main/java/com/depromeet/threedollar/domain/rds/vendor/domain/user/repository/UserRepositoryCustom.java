package com.depromeet.threedollar.domain.rds.vendor.domain.user.repository;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.vendor.domain.user.User;
import com.depromeet.threedollar.domain.rds.vendor.domain.user.UserSocialType;

public interface UserRepositoryCustom {

    boolean existsByName(String name);

    boolean existsBySocialIdAndSocialType(String socialId, UserSocialType socialType);

    @Nullable
    User findUserBySocialIdAndSocialType(String socialId, UserSocialType type);

    @Nullable
    User findUserById(Long userId);

    List<User> findAllByUserId(List<Long> userIds);

}
