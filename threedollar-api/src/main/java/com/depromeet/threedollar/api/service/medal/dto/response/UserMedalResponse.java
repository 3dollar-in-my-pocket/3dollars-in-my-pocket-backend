package com.depromeet.threedollar.api.service.medal.dto.response;

import com.depromeet.threedollar.domain.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.domain.medal.UserMedalType;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMedalResponse {

    private UserMedalType medalType;
    private String description;

    public static UserMedalResponse of(UserMedal userMedal) {
        return new UserMedalResponse(userMedal.getMedalType(), userMedal.getMedalType().getDescription());
    }

}
