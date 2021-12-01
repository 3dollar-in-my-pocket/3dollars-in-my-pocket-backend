package com.depromeet.threedollar.domain.domain.medal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMedalCollection {

    private Map<Long, UserMedal> cachedUserMedals;

    private UserMedalCollection(Map<Long, UserMedal> cachedUserMedals) {
        this.cachedUserMedals = cachedUserMedals;
    }

    public static UserMedalCollection of(List<UserMedal> userMedals) {
        return new UserMedalCollection(userMedals.stream()
            .collect(Collectors.toMap(UserMedal::getUserId, userMedal -> userMedal)));
    }

}
