package com.depromeet.threedollar.api.service.store.dto.response.deprecated;

import com.depromeet.threedollar.api.service.store.dto.response.StoreWithVisitsAndDistanceResponse;
import com.depromeet.threedollar.common.type.DistanceGroupType;
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
public class StoresGroupByDistanceV2Response {

    private final List<StoreWithVisitsAndDistanceResponse> storeList50 = new ArrayList<>();
    private final List<StoreWithVisitsAndDistanceResponse> storeList100 = new ArrayList<>();
    private final List<StoreWithVisitsAndDistanceResponse> storeList500 = new ArrayList<>();
    private final List<StoreWithVisitsAndDistanceResponse> storeList1000 = new ArrayList<>();
    private final List<StoreWithVisitsAndDistanceResponse> storeListOver1000 = new ArrayList<>();

    private StoresGroupByDistanceV2Response(List<StoreWithVisitsAndDistanceResponse> stores) {
        for (StoreWithVisitsAndDistanceResponse store : stores) {
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

    public static StoresGroupByDistanceV2Response of(List<StoreWithVisitsAndDistanceResponse> stores) {
        return new StoresGroupByDistanceV2Response(stores);
    }

}
