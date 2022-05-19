package com.depromeet.threedollar.api.user.service.store.dto.request;

import java.util.Comparator;
import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.api.user.service.store.dto.response.StoreWithVisitsAndDistanceResponse;
import com.depromeet.threedollar.api.user.service.store.dto.type.UserStoreOrderType;
import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.common.utils.distance.LookupRadiusDistanceLimiter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveAroundStoresRequest {

    @PositiveOrZero(message = "{store.distance.positiveOrZero}")
    @NotNull(message = "{store.distance.notNull}")
    private Double distance;

    @Nullable
    private UserMenuCategoryType category;

    @NotNull(message = "{store.orderType.notNull}")
    private UserStoreOrderType orderType = UserStoreOrderType.DISTANCE_ASC; // 호환성을 위해 기본적으로 거리순으로 정렬한다

    @Min(value = 1, message = "{common.size.min}")
    @Max(value = 100, message = "{common.size.max}")
    private int size = 100;

    @Builder(builderMethodName = "testBuilder")
    private RetrieveAroundStoresRequest(double distance, @Nullable UserMenuCategoryType category, UserStoreOrderType orderType, Integer size) {
        this.distance = distance;
        this.category = category;
        this.orderType = orderType;
        this.size = Optional.ofNullable(size).orElse(100);
    }

    public Double getDistance() {
        return LookupRadiusDistanceLimiter.fromMtoKm(distance);
    }

    public Comparator<StoreWithVisitsAndDistanceResponse> getSorted() {
        return this.orderType.getSorted();
    }

}
