package com.depromeet.threedollar.api.service.medal.dto.request;

import lombok.*;
import org.jetbrains.annotations.Nullable;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivateUserMedalRequest {

    @Nullable
    private Long medalId;

    public static ActivateUserMedalRequest testInstance(Long medalId) {
        return new ActivateUserMedalRequest(medalId);
    }

}
