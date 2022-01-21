package com.depromeet.threedollar.domain.user.domain.popup;

import com.depromeet.threedollar.common.docs.ObjectMother;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

@ObjectMother
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PopupCreator {

    public static Popup create(PopupPositionType popupPositionType, PopupPlatformType platformType, @Nullable String title,
                               @Nullable String subTitle, String imageUrl, @Nullable String linkUrl, @Nullable String bgColor, @Nullable String fontColor,
                               LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return Popup.builder()
            .title(title)
            .subTitle(subTitle)
            .positionType(popupPositionType)
            .platformType(platformType)
            .imageUrl(imageUrl)
            .linkUrl(linkUrl)
            .bgColor(bgColor)
            .fontColor(fontColor)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();
    }

}
