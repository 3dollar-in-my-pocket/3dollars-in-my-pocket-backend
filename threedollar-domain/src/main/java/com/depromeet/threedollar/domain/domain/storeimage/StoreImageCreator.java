package com.depromeet.threedollar.domain.domain.storeimage;

import com.depromeet.threedollar.common.docs.ObjectMother;
import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ObjectMother
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class StoreImageCreator {

    static StoreImage create(Store store, Long userId, String url) {
        return StoreImage.builder()
            .store(store)
            .userId(userId)
            .url(url)
            .build();
    }

}
