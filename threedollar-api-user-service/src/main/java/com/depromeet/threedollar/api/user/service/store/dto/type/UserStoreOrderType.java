package com.depromeet.threedollar.api.user.service.store.dto.type;

import java.util.Comparator;

import com.depromeet.threedollar.api.user.service.store.dto.response.StoreWithVisitsAndDistanceResponse;
import com.depromeet.threedollar.common.model.EnumModel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserStoreOrderType implements EnumModel {

    DISTANCE_ASC("가까운 거리순", Comparator.comparing(StoreWithVisitsAndDistanceResponse::getDistance)),
    REVIEW_DESC("높은 리뷰순", Comparator.comparing(StoreWithVisitsAndDistanceResponse::getRating).reversed()),
    ;

    private final String description;
    private final Comparator<StoreWithVisitsAndDistanceResponse> sorted;

    @Override
    public String getKey() {
        return name();
    }

}
