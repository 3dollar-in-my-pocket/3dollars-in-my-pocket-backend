package com.depromeet.threedollar.api.service.store.dto.request;

import com.depromeet.threedollar.api.service.store.dto.response.StoreWithDistanceResponse;
import com.depromeet.threedollar.api.service.store.dto.type.StoreOrderType;
import com.depromeet.threedollar.domain.domain.store.MenuCategoryType;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Comparator;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveNearStoresRequest {

    @NotNull(message = "{store.latitude.notNull}")
    private Double latitude;

    @NotNull(message = "{store.longitude.notNull}")
    private Double longitude;

    @NotNull(message = "{store.mapLatitude.notNull}")
    private Double mapLatitude;

    @NotNull(message = "{store.mapLongitude.notNull}")
    private Double mapLongitude;

    @NotNull(message = "{store.distance.notNull}")
    private Double distance;

    @Nullable
    private MenuCategoryType category;

    @NotNull(message = "{store.orderType.notNull}")
    private StoreOrderType orderType = StoreOrderType.DISTANCE_ASC; // 호환성을 위해 기본적으로 거리순으로 정렬한다

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    private RetrieveNearStoresRequest(double latitude, double longitude, double mapLatitude, double mapLongitude,
                                      double distance, @Nullable MenuCategoryType category, StoreOrderType orderType) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.mapLatitude = mapLatitude;
        this.mapLongitude = mapLongitude;
        this.distance = distance;
        this.category = category;
        this.orderType = orderType;
    }

    public double getDistance() {
        return this.distance / 1000;
    }

    public Comparator<StoreWithDistanceResponse> getSorted() {
        return this.orderType.getSorted();
    }

}
