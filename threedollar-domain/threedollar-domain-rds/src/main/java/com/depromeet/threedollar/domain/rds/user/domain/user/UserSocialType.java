package com.depromeet.threedollar.domain.rds.user.domain.user;

import com.depromeet.threedollar.common.model.EnumModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserSocialType implements EnumModel {

    KAKAO("카카오톡"),
    APPLE("애플"),
    GOOGLE("구글"),
    ;

    private final String description;

    @Override
    public String getKey() {
        return name();
    }

}
