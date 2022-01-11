package com.depromeet.threedollar.api.service.store.dto.type;

import com.depromeet.threedollar.api.service.store.dto.response.StoreWithVisitsAndDistanceResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum StoreOrderType {

    DISTANCE_ASC(Comparator.comparing(StoreWithVisitsAndDistanceResponse::getDistance)),
    REVIEW_DESC(Comparator.comparing(StoreWithVisitsAndDistanceResponse::getRating).reversed()),
    ;

    private final Comparator<StoreWithVisitsAndDistanceResponse> sorted;

}
