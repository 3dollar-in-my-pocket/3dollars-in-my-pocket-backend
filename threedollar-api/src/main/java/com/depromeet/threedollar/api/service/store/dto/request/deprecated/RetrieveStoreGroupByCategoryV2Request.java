package com.depromeet.threedollar.api.service.store.dto.request.deprecated;

import com.depromeet.threedollar.domain.domain.store.MenuCategoryType;
import lombok.*;

import javax.validation.constraints.NotNull;

@Deprecated
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveStoreGroupByCategoryV2Request {

    @NotNull(message = "{menu.category.notNull}")
    private MenuCategoryType category;

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public RetrieveStoreGroupByCategoryV2Request(MenuCategoryType category) {
        this.category = category;
    }

}
