package com.depromeet.threedollar.api.userservice.service.user.dto.request;

import com.depromeet.threedollar.api.userservice.config.vadlidator.NickName;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckAvailableNameRequest {

    @NickName
    private String name;

    @Builder(builderMethodName = "testBuilder")
    private CheckAvailableNameRequest(String name) {
        this.name = name;
    }

}
