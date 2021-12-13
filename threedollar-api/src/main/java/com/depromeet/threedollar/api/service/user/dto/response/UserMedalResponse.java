package com.depromeet.threedollar.api.service.user.dto.response;

import com.depromeet.threedollar.domain.domain.medal.Medal;
import com.depromeet.threedollar.domain.domain.medal.UserMedal;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMedalResponse {

    private static final String SIGN_OUT_MEDAL_NAME = "사무치게 그리운";

    @Nullable
    private Long medalId;

    private String name;

    @Nullable
    private String iconUrl;

    @Nullable
    private String disableIconUrl;

    @Builder(access = AccessLevel.PRIVATE)
    private UserMedalResponse(Long medalId, String name, String iconUrl, String disableIconUrl) {
        this.medalId = medalId;
        this.name = name;
        this.iconUrl = iconUrl;
        this.disableIconUrl = disableIconUrl;
    }

    public static UserMedalResponse of(@Nullable UserMedal userMedal) {
        if (userMedal == null) {
            return signOut();
        }
        return of(userMedal.getMedal());
    }

    public static UserMedalResponse of(@NotNull Medal medal) {
        return UserMedalResponse.builder()
            .medalId(medal.getId())
            .name(medal.getName())
            .iconUrl(medal.getActivationIconUrl())
            .disableIconUrl(medal.getDisableIconUrl())
            .build();
    }

    static UserMedalResponse signOut() {
        return UserMedalResponse.builder()
            .medalId(null)
            .name(SIGN_OUT_MEDAL_NAME)
            .iconUrl(null)
            .disableIconUrl(null)
            .build();
    }

}
