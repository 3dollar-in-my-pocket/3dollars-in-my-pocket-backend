package com.depromeet.threedollar.domain.domain.medal;

import com.depromeet.threedollar.domain.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMedalsCollection {

    private Map<Long, UserMedal> cachedUserMedals;

    public static UserMedalsCollection of(List<User> users) {
        List<UserMedal> userMedals = users.stream()
            .map(User::getActivatedMedal)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        return new UserMedalsCollection(userMedals.stream()
            .collect(Collectors.toMap(UserMedal::getUserId, userMedal -> userMedal)));
    }

    public UserMedal getUserMedal(Long userId) {
        if (userId == null) {
            return null;
        }
        return cachedUserMedals.get(userId);
    }

}
