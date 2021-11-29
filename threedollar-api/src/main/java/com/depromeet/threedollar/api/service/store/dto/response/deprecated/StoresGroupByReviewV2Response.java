package com.depromeet.threedollar.api.service.store.dto.response.deprecated;

import com.depromeet.threedollar.api.service.store.dto.response.StoreWithVisitsAndDistanceResponse;
import com.depromeet.threedollar.domain.domain.review.RatingGroupType;
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
public class StoresGroupByReviewV2Response {

    private final List<StoreWithVisitsAndDistanceResponse> storeList0 = new ArrayList<>();
    private final List<StoreWithVisitsAndDistanceResponse> storeList1 = new ArrayList<>();
    private final List<StoreWithVisitsAndDistanceResponse> storeList2 = new ArrayList<>();
    private final List<StoreWithVisitsAndDistanceResponse> storeList3 = new ArrayList<>();
    private final List<StoreWithVisitsAndDistanceResponse> storeList4 = new ArrayList<>();

    private StoresGroupByReviewV2Response(List<StoreWithVisitsAndDistanceResponse> stores) {
        for (StoreWithVisitsAndDistanceResponse store : stores) {
            RatingGroupType group = RatingGroupType.of(store.getRating());
            switch (group) {
                case ZERO_TO_ONE:
                    storeList0.add(store);
                    break;
                case ONE_TO_TWO:
                    storeList1.add(store);
                    break;
                case TWO_TO_THREE:
                    storeList2.add(store);
                    break;
                case THREE_TO_FOUR:
                    storeList3.add(store);
                    break;
                default:
                    storeList4.add(store);
                    break;
            }
        }
    }

    public static StoresGroupByReviewV2Response of(List<StoreWithVisitsAndDistanceResponse> stores) {
        return new StoresGroupByReviewV2Response(stores);
    }

}
