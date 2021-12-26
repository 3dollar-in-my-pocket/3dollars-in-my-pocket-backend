package com.depromeet.threedollar.api.service.medal.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangeRepresentativeMedalRequest {

    @NotNull(message = "{user.medalId.notNull}")
    private Long medalId;

    public static ChangeRepresentativeMedalRequest testInstance(Long medalId) {
        return new ChangeRepresentativeMedalRequest(medalId);
    }

}
