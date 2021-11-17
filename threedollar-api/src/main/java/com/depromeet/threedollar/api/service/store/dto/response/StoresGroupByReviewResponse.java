package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.domain.domain.review.RatingGroupType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoresGroupByReviewResponse {

    private final List<StoreInfoResponse> storeList0 = new ArrayList<>();
    private final List<StoreInfoResponse> storeList1 = new ArrayList<>();
    private final List<StoreInfoResponse> storeList2 = new ArrayList<>();
    private final List<StoreInfoResponse> storeList3 = new ArrayList<>();
    private final List<StoreInfoResponse> storeList4 = new ArrayList<>();

    private StoresGroupByReviewResponse(List<StoreInfoResponse> stores) {
        for (StoreInfoResponse store : stores) {
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

    public static StoresGroupByReviewResponse of(List<StoreInfoResponse> stores) {
        return new StoresGroupByReviewResponse(stores);
    }

}
