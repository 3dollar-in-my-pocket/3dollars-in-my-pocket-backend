package com.depromeet.threedollar.api.service.medal.dto.response;

import com.depromeet.threedollar.domain.domain.medal.UserMedalType;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMedalResponse {

    private UserMedalType medalType;
    private String description;

    public static UserMedalResponse of(UserMedalType userMedalType) {
        return new UserMedalResponse(userMedalType, userMedalType.getDescription());
    }

}
