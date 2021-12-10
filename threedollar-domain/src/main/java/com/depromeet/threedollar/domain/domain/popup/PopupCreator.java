package com.depromeet.threedollar.domain.domain.popup;

import com.depromeet.threedollar.common.docs.ObjectMother;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@ObjectMother
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PopupCreator {

    public static Popup create(String imageUrl, String linkUrl, LocalDateTime startDateTime, LocalDateTime endDateTime, PopupPlatformType platformType) {
        return Popup.builder()
            .imageUrl(imageUrl)
            .linkUrl(linkUrl)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .platformType(platformType)
            .build();
    }

}
