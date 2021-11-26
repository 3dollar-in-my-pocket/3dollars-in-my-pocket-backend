package com.depromeet.threedollar.domain.domain.medal;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMedalCreator {

    public static UserMedal create(Long userId, UserMedalType medalType) {
    return UserMedal.of(userId, medalType);
    }

}
