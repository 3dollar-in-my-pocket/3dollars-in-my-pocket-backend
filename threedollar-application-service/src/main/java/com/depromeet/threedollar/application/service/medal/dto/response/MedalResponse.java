package com.depromeet.threedollar.application.service.medal.dto.response;

import com.depromeet.threedollar.domain.domain.medal.Medal;
import com.depromeet.threedollar.domain.domain.medal.UserMedal;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MedalResponse {

    private static final String SIGN_OUT_MEDAL_NAME = "사무치게 그리운";

    private Long medalId;

    private String name;

    private String iconUrl;

    private String disableIconUrl;

    // TODO: 차후 마이그레이션 이후 Not-Null
    public static MedalResponse of(@Nullable UserMedal userMedal) {
        if (userMedal == null) {
            return signOut();
        }
        return of(userMedal.getMedal());
    }

    public static MedalResponse of(@NotNull Medal medal) {
        return new MedalResponse(medal.getId(), medal.getName(), medal.getActivationIconUrl(), medal.getDisableIconUrl());
    }

    public static MedalResponse signOut() {
        return new MedalResponse(null, SIGN_OUT_MEDAL_NAME, null, null);
    }

}
