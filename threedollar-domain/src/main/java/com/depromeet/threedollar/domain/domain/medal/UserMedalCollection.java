package com.depromeet.threedollar.domain.domain.medal;

import com.depromeet.threedollar.domain.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMedalCollection {

    private Map<Long, UserMedal> cachedUserMedals;

    private UserMedalCollection(Map<Long, UserMedal> cachedUserMedals) {
        this.cachedUserMedals = cachedUserMedals;
    }

    public static UserMedalCollection of(List<User> users) {
        List<UserMedal> userMedals = users.stream()
            .map(User::getActivatedMedal)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        return new UserMedalCollection(userMedals.stream()
            .collect(Collectors.toMap(UserMedal::getUserId, userMedal -> userMedal)));
    }

}
