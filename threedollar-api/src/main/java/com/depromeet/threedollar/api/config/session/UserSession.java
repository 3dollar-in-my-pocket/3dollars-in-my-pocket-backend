package com.depromeet.threedollar.api.config.session;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSession implements Serializable {

    private final Long userId;

    public static UserSession of(Long userId) {
        return new UserSession(userId);
    }

}
