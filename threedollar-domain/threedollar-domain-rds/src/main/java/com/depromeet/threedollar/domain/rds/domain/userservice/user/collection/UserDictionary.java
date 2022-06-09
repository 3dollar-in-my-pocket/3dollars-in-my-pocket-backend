package com.depromeet.threedollar.domain.rds.domain.userservice.user.collection;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

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
