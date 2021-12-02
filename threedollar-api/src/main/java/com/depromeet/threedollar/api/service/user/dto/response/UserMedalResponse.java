package com.depromeet.threedollar.api.service.user.dto.response;

import com.depromeet.threedollar.domain.domain.medal.UserMedal;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMedalResponse {

    private Long userMedalId;

    private String name;

    private String iconUrl;

    public static UserMedalResponse of(UserMedal userMedal) {
        if (userMedal == null) {
            return null;
        }
        return new UserMedalResponse(userMedal.getId(), userMedal.getMedalName(), userMedal.getMedalIconUrl());
    }

}
