package com.depromeet.threedollar.api.user.service.store.dto.request;

import com.depromeet.threedollar.api.user.service.store.dto.response.StoreWithVisitsAndDistanceResponse;
import com.depromeet.threedollar.api.user.service.store.dto.type.StoreOrderType;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRadiusDistance;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Comparator;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveNearStoresRequest {

    @PositiveOrZero(message = "{store.distance.positiveOrZero}")
    @NotNull(message = "{store.distance.notNull}")
    private Double distance;

    @Nullable
    private MenuCategoryType category;

    @NotNull(message = "{store.orderType.notNull}")
    private StoreOrderType orderType = StoreOrderType.DISTANCE_ASC; // 호환성을 위해 기본적으로 거리순으로 정렬한다

    @Builder(builderMethodName = "testBuilder")
    private RetrieveNearStoresRequest(double distance, @Nullable MenuCategoryType category, StoreOrderType orderType) {
        this.distance = distance;
        this.category = category;
        this.orderType = orderType;
    }

    public StoreRadiusDistance getDistance() {
        return StoreRadiusDistance.of(this.distance / 1000);
    }

    public Comparator<StoreWithVisitsAndDistanceResponse> getSorted() {
        return this.orderType.getSorted();
    }

}
