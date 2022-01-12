package com.depromeet.threedollar.api.service.user.dto.request;

import com.depromeet.threedollar.application.config.validator.NickName;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateUserInfoRequest {

    @NickName
    private String name;

    public static UpdateUserInfoRequest testInstance(String name) {
        return new UpdateUserInfoRequest(name);
    }

}
