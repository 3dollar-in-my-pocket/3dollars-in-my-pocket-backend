package com.depromeet.threedollar.domain.rds.user.collection.user;

import com.depromeet.threedollar.domain.rds.user.domain.user.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDictionary {

    private final Map<Long, User> dictionary;

    public static UserDictionary of(List<User> users) {
        return new UserDictionary(users.stream()
            .collect(Collectors.toMap(User::getId, user -> user)));
    }

    @Nullable
    public User getUser(Long userId) {
        return dictionary.get(userId);
    }

}
