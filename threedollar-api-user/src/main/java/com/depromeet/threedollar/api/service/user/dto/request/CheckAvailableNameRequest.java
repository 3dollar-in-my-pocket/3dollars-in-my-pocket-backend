package com.depromeet.threedollar.api.service.user.dto.request;

import com.depromeet.threedollar.application.config.validator.NickName;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckAvailableNameRequest {

    @NickName
    private String name;

    public static CheckAvailableNameRequest testInstance(String name) {
        return new CheckAvailableNameRequest(name);
    }

}
