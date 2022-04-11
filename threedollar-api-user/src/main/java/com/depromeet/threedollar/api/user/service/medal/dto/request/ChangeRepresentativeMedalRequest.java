package com.depromeet.threedollar.api.user.service.medal.dto.request;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
