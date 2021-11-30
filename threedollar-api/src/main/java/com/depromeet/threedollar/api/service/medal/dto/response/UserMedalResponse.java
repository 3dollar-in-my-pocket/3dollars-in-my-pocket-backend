package com.depromeet.threedollar.api.service.medal.dto.response;

import com.depromeet.threedollar.domain.domain.medal.UserMedalType;
import lombok.*;
import org.jetbrains.annotations.Nullable;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMedalResponse {

    private UserMedalType medalType;
    private String description;

    public static UserMedalResponse of(@Nullable UserMedalType userMedalType) {
        if (userMedalType == null) {
            return null;
        }
        return new UserMedalResponse(userMedalType, userMedalType.getDescription());
    }

}
