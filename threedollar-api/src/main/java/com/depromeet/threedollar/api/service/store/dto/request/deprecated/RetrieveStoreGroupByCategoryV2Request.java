package com.depromeet.threedollar.api.service.store.dto.request.deprecated;

import com.depromeet.threedollar.domain.domain.store.MenuCategoryType;
import lombok.*;

import javax.validation.constraints.NotNull;

@Deprecated
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveStoreGroupByCategoryV2Request {

    @NotNull(message = "{store.latitude.notNull}")
    private Double latitude;

    @NotNull(message = "{store.longitude.notNull}")
    private Double longitude;

    @NotNull(message = "{store.mapLatitude.notNull}")
    private Double mapLatitude;

    @NotNull(message = "{store.mapLongitude.notNull}")
    private Double mapLongitude;

    @NotNull(message = "{menu.category.notNull}")
    private MenuCategoryType category;

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public RetrieveStoreGroupByCategoryV2Request(Double latitude, Double longitude, Double mapLatitude, Double mapLongitude, MenuCategoryType category) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.mapLatitude = mapLatitude;
        this.mapLongitude = mapLongitude;
        this.category = category;
    }

}
