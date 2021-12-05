package com.depromeet.threedollar.domain.domain.storeimage;

import com.depromeet.threedollar.common.docs.ObjectMother;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ObjectMother
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class StoreImageCreator {

    static StoreImage create(Long storeId, Long userId, String url) {
        return StoreImage.builder()
            .storeId(storeId)
            .userId(userId)
            .url(url)
            .build();
    }

}
