package com.depromeet.threedollar.domain.rds.event.userservice.user;

import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class NewUserCreatedEvent {

    private final Long userId;

    @Builder(access = AccessLevel.PRIVATE)
    private NewUserCreatedEvent(Long userId) {
        this.userId = userId;
    }

    public static NewUserCreatedEvent of(User user) {
        return NewUserCreatedEvent.builder()
            .userId(user.getId())
            .build();
    }

}
