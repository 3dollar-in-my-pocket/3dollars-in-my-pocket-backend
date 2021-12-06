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

    private Long medalId;

    private String name;

    private String iconUrl;

    public static MedalResponse of(@Nullable UserMedal userMedal) {
        if (userMedal == null) {
            return null;
        }
        return of(userMedal.getMedal());
    }

    public static MedalResponse of(@NotNull Medal medal) {
        return new MedalResponse(medal.getId(), medal.getName(), medal.getIconUrl());
    }

}
