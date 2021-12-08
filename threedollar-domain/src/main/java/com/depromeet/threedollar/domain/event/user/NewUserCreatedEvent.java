package com.depromeet.threedollar.domain.event.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NewUserCreatedEvent {

    private final Long userId;

    public static NewUserCreatedEvent of(Long userId) {
        return new NewUserCreatedEvent(userId);
    }

}
