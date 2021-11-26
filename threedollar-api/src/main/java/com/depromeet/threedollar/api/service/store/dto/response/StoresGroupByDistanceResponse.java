package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.domain.domain.common.DistanceGroupType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoresGroupByDistanceResponse {

    private final List<StoreWithDistanceResponse> storeList50 = new ArrayList<>();
    private final List<StoreWithDistanceResponse> storeList100 = new ArrayList<>();
    private final List<StoreWithDistanceResponse> storeList500 = new ArrayList<>();
    private final List<StoreWithDistanceResponse> storeList1000 = new ArrayList<>();
    private final List<StoreWithDistanceResponse> storeListOver1000 = new ArrayList<>();

    private StoresGroupByDistanceResponse(List<StoreWithDistanceResponse> stores) {
        for (StoreWithDistanceResponse store : stores) {
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
                default:
                    storeListOver1000.add(store);
                    break;
            }
        }
    }

    public static StoresGroupByDistanceResponse of(List<StoreWithDistanceResponse> stores) {
        return new StoresGroupByDistanceResponse(stores);
    }

}
