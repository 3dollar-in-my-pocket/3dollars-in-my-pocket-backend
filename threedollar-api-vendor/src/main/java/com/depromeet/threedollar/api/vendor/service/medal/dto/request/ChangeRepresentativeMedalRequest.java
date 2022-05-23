package com.depromeet.threedollar.api.vendor.service.medal.dto.request;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangeRepresentativeMedalRequest {

    @NotNull(message = "{user.medalId.notNull}")
    private Long medalId;

    @Builder(builderMethodName = "testBuilder")
    private ChangeRepresentativeMedalRequest(Long medalId) {
        this.medalId = medalId;
    }

}
