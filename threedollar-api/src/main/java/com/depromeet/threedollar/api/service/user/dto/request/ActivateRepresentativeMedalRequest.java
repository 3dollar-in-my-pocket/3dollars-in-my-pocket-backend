package com.depromeet.threedollar.api.service.user.dto.request;

import lombok.*;
import org.jetbrains.annotations.Nullable;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivateRepresentativeMedalRequest {

    @Nullable
    private Long medalId;

    public static ActivateRepresentativeMedalRequest testInstance(Long medalId) {
        return new ActivateRepresentativeMedalRequest(medalId);
    }

}
