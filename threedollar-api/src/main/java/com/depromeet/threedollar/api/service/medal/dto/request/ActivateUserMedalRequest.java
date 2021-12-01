package com.depromeet.threedollar.api.service.medal.dto.request;

import lombok.*;
import org.jetbrains.annotations.Nullable;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivateUserMedalRequest {

    @Nullable
    private Long userMedalId;

    public static ActivateUserMedalRequest testInstance(Long userMedalId) {
        return new ActivateUserMedalRequest(userMedalId);
    }

}
