package com.depromeet.threedollar.domain.domain.store;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StoreType {

	ROAD("길거리"),
	STORE("매장"),
	CONVENIENCE_STORE("편의점");

	private final String type;

}