package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.common.type.DistanceGroupType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoresGroupByDistanceResponse {

	private final List<StoreInfoResponse> storeList50 = new ArrayList<>();
	private final List<StoreInfoResponse> storeList100 = new ArrayList<>();
	private final List<StoreInfoResponse> storeList500 = new ArrayList<>();
	private final List<StoreInfoResponse> storeList1000 = new ArrayList<>();
	private final List<StoreInfoResponse> storeListOver1000 = new ArrayList<>();

	private StoresGroupByDistanceResponse(List<StoreInfoResponse> stores) {
		for (StoreInfoResponse store : stores) {
			DistanceGroupType group = DistanceGroupType.of(store.getDistance());
			switch (group) {
				case UNDER_FIFTY:
					storeList50.add(store);
					break;
				case FIFTY_TO_HUNDRED:
					storeList100.add(store);
					break;
				case HUNDRED_TO_FIVE_HUNDRED:
					storeList500.add(store);
					break;
				case FIVE_HUNDRED_TO_THOUSAND:
					storeList1000.add(store);
					break;
				case OVER_THOUSAND:
					storeListOver1000.add(store);
					break;
				default:
					throw new IllegalArgumentException(String.format("예상치 못한 거리가 입력되었습니다 (%s)", store.getDistance()));
			}
		}
	}

	public static StoresGroupByDistanceResponse of(List<StoreInfoResponse> stores) {
		return new StoresGroupByDistanceResponse(stores);
	}

}
