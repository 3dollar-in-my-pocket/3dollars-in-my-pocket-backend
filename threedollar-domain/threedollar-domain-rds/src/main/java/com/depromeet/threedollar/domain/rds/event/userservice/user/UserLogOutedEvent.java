package com.depromeet.threedollar.domain.rds.event.userservice.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UserLogOutedEvent {

    private final Long userId;

    @Builder(access = AccessLevel.PRIVATE)
    private UserLogOutedEvent(Long userId) {
        this.userId = userId;
    }

    public static UserLogOutedEvent of(Long userId) {
        return UserLogOutedEvent.builder()
            .userId(userId)
            .build();
    }

}
