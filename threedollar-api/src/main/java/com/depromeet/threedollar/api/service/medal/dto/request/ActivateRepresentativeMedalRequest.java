package com.depromeet.threedollar.api.service.medal.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivateRepresentativeMedalRequest {

    @NotNull(message = "{user.medalId.notNull}")
    private Long medalId;

    public static ActivateRepresentativeMedalRequest testInstance(Long medalId) {
        return new ActivateRepresentativeMedalRequest(medalId);
    }

}
