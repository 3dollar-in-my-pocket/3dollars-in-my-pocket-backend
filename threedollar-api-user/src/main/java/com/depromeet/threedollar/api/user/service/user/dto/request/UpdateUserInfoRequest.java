package com.depromeet.threedollar.api.user.service.user.dto.request;

import com.depromeet.threedollar.api.user.config.vadlidator.NickName;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
