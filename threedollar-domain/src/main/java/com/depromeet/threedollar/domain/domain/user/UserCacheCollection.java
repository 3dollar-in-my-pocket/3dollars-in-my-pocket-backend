package com.depromeet.threedollar.domain.domain.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCacheCollection {

    private Map<Long, User> cachedUser;

    public static UserCacheCollection of(List<User> users) {
        return new UserCacheCollection(users.stream()
            .collect(Collectors.toMap(User::getId, user -> user)));
    }

    public User getUser(Long userId) {
        return cachedUser.get(userId);
    }

}
