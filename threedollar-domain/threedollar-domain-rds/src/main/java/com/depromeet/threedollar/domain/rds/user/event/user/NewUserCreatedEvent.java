package com.depromeet.threedollar.domain.rds.user.event.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NewUserCreatedEvent {

    private final Long userId;

    @Builder(access = AccessLevel.PRIVATE)
    private NewUserCreatedEvent(Long userId) {
        this.userId = userId;
    }

    public static NewUserCreatedEvent of(Long userId) {
        return NewUserCreatedEvent.builder()
            .userId(userId)
            .build();
    }

}
