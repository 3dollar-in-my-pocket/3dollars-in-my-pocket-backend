package com.depromeet.threedollar.domain.user.domain.storedelete;

import com.depromeet.threedollar.common.docs.ObjectMother;
import com.depromeet.threedollar.domain.user.domain.store.Store;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ObjectMother
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreDeleteRequestCreator {

    public static StoreDeleteRequest create(Store store, Long userId, DeleteReasonType type) {
        return StoreDeleteRequest.of(store, userId, type);
    }

}
