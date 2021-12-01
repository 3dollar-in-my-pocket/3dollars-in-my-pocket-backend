package com.depromeet.threedollar.api.service.medal.dto.response;

import com.depromeet.threedollar.domain.domain.medal.Medal;
import com.depromeet.threedollar.domain.domain.medal.UserMedal;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMedalResponse {

    private Long medalId;

    private String name;

    private String iconUrl;

    public static UserMedalResponse of(UserMedal userMedal) {
        if (userMedal == null) {
            return null;
        }
        return of(userMedal.getMedal());
    }

    private static UserMedalResponse of(Medal medal) {
        return new UserMedalResponse(medal.getId(), medal.getName(), medal.getIconUrl());
    }

}
