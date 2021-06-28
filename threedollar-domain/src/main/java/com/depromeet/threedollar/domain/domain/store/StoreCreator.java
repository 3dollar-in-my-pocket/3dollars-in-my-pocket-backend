package com.depromeet.threedollar.domain.domain.store;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreCreator {

	public static Store create(Long userId, String storeName) {
		return Store.builder()
				.userId(userId)
				.storeName(storeName)
				.storeType(StoreType.STORE)
				.latitude(33.0)
				.longitude(124.0)
				.build();
	}

}
