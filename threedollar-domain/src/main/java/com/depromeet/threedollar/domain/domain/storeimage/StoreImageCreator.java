package com.depromeet.threedollar.domain.domain.storeimage;

import com.depromeet.threedollar.common.docs.ObjectMother;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ObjectMother
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreImageCreator {

    public static StoreImage create(Long storeId, Long userId, String url) {
        return StoreImage.builder()
            .storeId(storeId)
            .userId(userId)
            .url(url)
            .build();
    }

}
