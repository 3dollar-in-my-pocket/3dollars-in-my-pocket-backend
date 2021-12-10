package com.depromeet.threedollar.domain.collection.user;

import com.depromeet.threedollar.domain.domain.user.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCacheCollection {

    private final Map<Long, User> cachedUser;

    public static UserCacheCollection of(List<User> users) {
        return new UserCacheCollection(users.stream()
            .collect(Collectors.toMap(User::getId, user -> user)));
    }

    public User getUser(Long userId) {
        return cachedUser.get(userId);
    }

}
