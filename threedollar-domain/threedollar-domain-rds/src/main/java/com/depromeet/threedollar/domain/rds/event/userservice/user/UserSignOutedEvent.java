package com.depromeet.threedollar.domain.rds.event.userservice.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UserSignOutedEvent {

    private final Long userId;

    @Builder(access = AccessLevel.PRIVATE)
    private UserSignOutedEvent(Long userId) {
        this.userId = userId;
    }

    public static UserSignOutedEvent of(Long userId) {
        return UserSignOutedEvent.builder()
            .userId(userId)
            .build();
    }

}
