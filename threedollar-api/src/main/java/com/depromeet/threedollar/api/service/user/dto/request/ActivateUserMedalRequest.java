package com.depromeet.threedollar.api.service.user.dto.request;

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
