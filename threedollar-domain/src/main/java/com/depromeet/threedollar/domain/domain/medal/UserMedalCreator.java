package com.depromeet.threedollar.domain.domain.medal;

import com.depromeet.threedollar.domain.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMedalCreator {

    public static UserMedal createActive(Medal medal, User user) {
        return new UserMedal(medal, user, UserMedalStatus.ACTIVE);
    }

    public static UserMedal createInActive(Medal medal, User user) {
        return new UserMedal(medal, user, UserMedalStatus.IN_ACTIVE);
    }

}
